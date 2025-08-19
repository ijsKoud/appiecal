package nl.klrnbk.daan.appiecal.apps.calendar.api.service

import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.DateTime
import net.fortuna.ical4j.model.TimeZoneRegistryFactory
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.CalScale
import net.fortuna.ical4j.model.property.Description
import net.fortuna.ical4j.model.property.ProdId
import net.fortuna.ical4j.model.property.Uid
import net.fortuna.ical4j.model.property.Version
import net.fortuna.ical4j.model.property.immutable.ImmutableCalScale
import net.fortuna.ical4j.model.property.immutable.ImmutableVersion
import nl.klrnbk.daan.appiecal.packages.common.exceptions.ApiException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.io.StringReader
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.Temporal
import java.util.UUID

@Service
class IcalService {
    fun getCalendarWithEventIdFromIcal(icalContent: String): Pair<Calendar, String> {
        val (calendar, vEvent) = getCalendarWithFirstEvent(icalContent)
        val id = getVEventId(vEvent)

        return Pair(calendar, id)
    }

    fun getVEventId(vEvent: VEvent): String {
        if (vEvent.uid.isEmpty) return UUID.randomUUID().toString()
        return vEvent.uid.get().value
    }

    fun getCalendarWithFirstEvent(content: String): Pair<Calendar, VEvent> {
        val parser = CalendarBuilder()
        val calendar = parser.build(StringReader(content))

        val event = calendar.getComponents<VEvent>("VEVENT").firstOrNull()
        if (event == null) {
            throw ApiException(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Received malformed body",
                "Expected event but got null instead",
            )
        }

        return Pair(calendar, event)
    }

    fun createEvent(
        eventId: String,
        title: String,
        description: String,
        start: ZonedDateTime,
        end: ZonedDateTime,
    ): Calendar {
        val calendar = Calendar()
        calendar.add<VEvent>(ProdId("-//Daan Klarenbeek//Appiecal 2.0//EN"))
        calendar.add<VEvent>(ImmutableVersion.VERSION_2_0)
        calendar.add<VEvent>(ImmutableCalScale.GREGORIAN)

        val startDate = start.withZoneSameInstant(ZoneId.of("UTC")) as Temporal
        val endDate = end.withZoneSameInstant(ZoneId.of("UTC")) as Temporal

        val timezoneRegistry = TimeZoneRegistryFactory.getInstance().createRegistry()
        val timezone = timezoneRegistry.getTimeZone("UTC")
        val vtimezone = timezone.vTimeZone

        val event = VEvent(startDate, endDate, title)
        event.add<VEvent>(Description(description))
        event.add<VEvent>(Uid(eventId))

        calendar.add<Calendar>(vtimezone)
        calendar.add<Calendar>(event)
        return calendar
    }
}
