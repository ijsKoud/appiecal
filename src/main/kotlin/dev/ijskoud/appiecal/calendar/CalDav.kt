package dev.ijskoud.appiecal.calendar

import biweekly.Biweekly
import biweekly.component.VEvent
import dev.ijskoud.appiecal.ah.rooster.Shift
import dev.ijskoud.appiecal.calendar.errors.EventDeleteError
import dev.ijskoud.appiecal.calendar.errors.EventUpdateCreateError
import dev.ijskoud.appiecal.calendar.methods.HttpReport
import dev.ijskoud.appiecal.store.caldav.CalDavStore
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.CredentialsProvider
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPut
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.slf4j.LoggerFactory
import java.io.StringReader
import java.net.URI
import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.StartElement


class CalDav {
    val store = CalDavStore.getInstance()
    private val logger = LoggerFactory.getLogger(CalDav::class.java)

    /**
     * Deletes an event from the calendar
     * @param id The id of the event you want to delete
     * @throws EventDeleteError If the event could not be deleted
     */
    fun deleteEvent(id: String) {
        val credentials = store.get()

        createAuthenticatedClient(credentials.username, credentials.password).use { client ->
            val request = HttpDelete(credentials.url + "/$id.ics")
            val response = client.execute(request)

            if (response.statusLine.statusCode > 299) {
                throw EventDeleteError(response.statusLine.reasonPhrase.toString())
            }

            logger.info("Shift deleted: id=$id")
        }
    }

    /**
     * Gets details of an individual event
     * @param id The id of the event to retrieve
     * @return VEvent The event data
     */
    fun getEvent(id: String): VEvent {
        val credentials = store.get()
        createAuthenticatedClient(credentials.username, credentials.password).use { client ->
            val request = HttpGet(credentials.url + "/$id.ics")
            val response = client.execute(request)
            val responseString = EntityUtils.toString(response.entity)

            val cal = Biweekly.parse(StringReader(responseString)).first()
            return cal.events[0];
        }
    }

    /**
     * Gets a list of events from the caldav server
     * @param ids The ids of the events you want to retrieve
     * @return The events that have been retrieved and parsed
     */
    fun getEvents(ids: Array<String>): List<VEvent> {
        val credentials = store.get()
        val uri = URI(credentials.url)

        val requestBody = """
            <c:calendar-multiget xmlns:c="urn:ietf:params:xml:ns:caldav" xmlns:d="DAV:">
            	<d:prop>
            		<d:getetag/>
            		<c:calendar-data></c:calendar-data>
            	</d:prop>
            	${ids.joinToString("\n") { id -> "<d:href>${uri.path}/${id}.ics</d:href>" }}
            </c:calendar-multiget>
        """.trimIndent()

        createAuthenticatedClient(credentials.username, credentials.password).use { client ->
            val request = HttpReport(credentials.url)
            request.setHeader("Depth", "1")
            request.setHeader("Content-Type", "text/xml")
            request.entity = StringEntity(requestBody, "UTF-8")

            val response = client.execute(request)
            val responseString = EntityUtils.toString(response.entity)
            val parsedData = parseEventXML(responseString)

            return parsedData.map { data -> Biweekly.parse(data).first().events[0] }
        }
    }

    /**
     * Updates or creates a new shift
     * @param shift The shift that needs to be updated/created
     */
    fun putEvent(shift: Shift) {
        // Skip leave and sick days
        if (shift.isLeaveDay() || shift.isSickDay()) return;

        val credentials = store.get()
        createAuthenticatedClient(credentials.username, credentials.password).use { client ->
            val request = HttpPut(credentials.url + "/${shift.id}.ics")
            request.setHeader("Content-Type", "text/calendar")
            request.entity = StringEntity(shift.toIcal(), "UTF-8")

            val response = client.execute(request)
            if (response.statusLine.statusCode > 299) {
                throw EventUpdateCreateError(response.statusLine.reasonPhrase.toString())
            }

            logger.info("Shift updated: id=${shift.id}, startDate=${shift.startDate}, endDate=${shift.endDate}")
        }
    }

    /**
     * Parses the XML data containing event information
     * @param xml The XML to parse
     * @return A list of events extracted from the XML
     */
    private fun parseEventXML(xml: String): List<String> {
        val xmlInputFactory = XMLInputFactory.newInstance()
        val reader: XMLEventReader = xmlInputFactory.createXMLEventReader(StringReader(xml))
        val events: ArrayList<String> = ArrayList<String>()

        // Extract the event data
        while (reader.hasNext()) {
            var event = reader.nextEvent()

            if (event.isStartElement) {
                val startElement: StartElement = event.asStartElement()
                if (startElement.name.localPart.equals("calendar-data")) {
                    event = reader.nextEvent()
                    events.add(event.asCharacters().data.toString())
                }
            }
        }

        return events;
    }

    private fun createAuthenticatedClient(username: String?, password: String?): CloseableHttpClient {
        val provider: CredentialsProvider = BasicCredentialsProvider()
        provider.setCredentials(AuthScope.ANY, UsernamePasswordCredentials(username, password))

        return HttpClients.custom()
            .setDefaultCredentialsProvider(provider)
            .build()
    }
}
