package nl.klrnbk.daan.appiecal.apps.calendar.api.service

import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.component.VEvent
import nl.klrnbk.daan.appiecal.packages.common.exceptions.ApiException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.io.StringReader
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
}
