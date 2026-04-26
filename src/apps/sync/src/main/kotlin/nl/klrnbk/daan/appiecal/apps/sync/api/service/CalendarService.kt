package nl.klrnbk.daan.appiecal.apps.sync.api.service

import nl.klrnbk.daan.appiecal.apps.sync.api.models.store.StoreInformation
import nl.klrnbk.daan.appiecal.apps.sync.clients.calendar.CalendarClient
import nl.klrnbk.daan.appiecal.apps.sync.helpers.getEventDescriptionFromShift
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponseShift
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponseShiftActivity
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.time.temporal.ChronoField

@Service
class CalendarService(
    private val calendarClient: CalendarClient,
) {
    fun getCurrentWeekDates(): Pair<ZonedDateTime, ZonedDateTime> {
        val now =
            ZonedDateTime
                .now()
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)

        val startOfWeek = now.with(ChronoField.DAY_OF_WEEK, 1)
        val endOfWeek = startOfWeek.plusWeeks(1)

        return Pair(startOfWeek, endOfWeek)
    }

    fun deleteEvents(
        authentication: String,
        userId: String,
        eventIds: List<String>,
    ) {
        eventIds.forEach { calendarClient.deleteEvent(authentication, userId, it) }
    }

    fun createOrUpdateEvent(
        authentication: String,
        userId: String,
        shift: ScheduleResponseShift<ScheduleResponseShiftActivity>,
        storesInformation: List<StoreInformation>,
    ): String {
        val store = storesInformation.find { it.id == shift.storeId }
        if (store == null) throw IllegalStateException("No store with id ${shift.storeId}")

        val title = "Shift at ${store.name} (${shift.storeId})"
        val description = getEventDescriptionFromShift(shift, store)

        return calendarClient.createEvent(
            authentication,
            userId,
            shift.id.toString(),
            title,
            description,
            store.address,
            shift.startDate,
            shift.endDate,
        )
    }
}
