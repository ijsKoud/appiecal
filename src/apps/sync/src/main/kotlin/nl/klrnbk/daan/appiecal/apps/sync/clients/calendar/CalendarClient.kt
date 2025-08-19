package nl.klrnbk.daan.appiecal.apps.sync.clients.calendar

import nl.klrnbk.daan.appiecal.packages.common.retrofit.RetrofitClient
import nl.klrnbk.daan.appiecal.packages.common.shared.services.calendar.models.CreateEventRequestBody
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponse
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponseShift
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponseShiftActivity
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.syncing.SyncStatusResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.ZonedDateTime

@Component
class CalendarClient(
    @Value("\${appiecal.sync.service-urls.calendar}") baseUrl: String,
) : RetrofitClient() {
    private val apiClient = getRetrofitClient(CalendarInterface::class.java, baseUrl)

    fun deleteEvent(
        token: String,
        userId: String,
        eventId: String,
    ) {
        val call = apiClient.deleteEvent(token, userId, eventId)
        handleApiCall(call)
    }

    fun createEvent(
        token: String,
        userId: String,
        eventId: String,
        title: String,
        description: String,
        location: String,
        startDate: ZonedDateTime,
        endDate: ZonedDateTime,
    ): String {
        val body =
            CreateEventRequestBody(
                eventId = eventId,
                title = title,
                description = description,
                location = location,
                startDate = startDate,
                endDate = endDate,
            )

        val call = apiClient.createEvent(token, userId, body)
        return handleApiCall(call)
    }
}
