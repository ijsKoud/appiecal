package nl.klrnbk.daan.appiecal.apps.schedule.api.service

import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.GqlClient
import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.schedule.GqlScheduleResponseSchedule
import nl.klrnbk.daan.appiecal.apps.schedule.constants.DATE_TIME_FORMATTER
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class GqlService(
    val gqlClient: GqlClient,
) {
    fun getRawSchedule(
        token: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): List<GqlScheduleResponseSchedule> {
        val startDateString = startDate.format(DATE_TIME_FORMATTER)
        val endDateString = endDate.format(DATE_TIME_FORMATTER)

        val response = gqlClient.getSchedule(token, startDateString, endDateString)
        return response.data.scheduleOverview.shifts
    }
}
