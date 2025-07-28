package nl.klrnbk.daan.appiecal.apps.calendar.api.service

import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.component.VEvent
import nl.klrnbk.daan.appiecal.apps.calendar.api.models.CalendarListEntryResponse
import nl.klrnbk.daan.appiecal.apps.calendar.clients.caldav.CaldavClient
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.springframework.stereotype.Service

@Service
class CaldavService(
    private val caldavClient: CaldavClient,
) {
    fun getPrincipal(
        baseUrl: String,
        authScope: String,
        authToken: String,
    ) = caldavClient.getPrincipal(baseUrl, authScope, authToken)

    fun getCalendarHomeSet(
        url: String,
        authScope: String,
        authToken: String,
    ) = caldavClient.getCalendarHomeSet(url, authScope, authToken)

    fun getCalendarList(
        url: String,
        authScope: String,
        authToken: String,
    ): List<CalendarListEntryResponse> {
        val response = caldavClient.getCalendarList(url, authScope, authToken)
        return response.map { CalendarListEntryResponse(it.first, it.second) }
    }

    fun deleteEvent(
        url: String,
        authScope: String,
        authToken: String,
        eventId: String,
    ) {
        val deleteUrl =
            url
                .toHttpUrl()
                .newBuilder()
                .addPathSegment("$eventId.ics")
                .build()
                .toString()

        caldavClient.deleteEvent(deleteUrl, authScope, authToken)
    }

    fun createEvent(
        url: String,
        authScope: String,
        authToken: String,
        eventId: String,
        calendar: Calendar,
    ) {
        val createUrl =
            url
                .toHttpUrl()
                .newBuilder()
                .addPathSegment("$eventId.ics")
                .build()
                .toString()

        val body = calendar.toString()
        caldavClient
            .createOrUpdateEvent(createUrl, authScope, authToken, body)
    }
}
