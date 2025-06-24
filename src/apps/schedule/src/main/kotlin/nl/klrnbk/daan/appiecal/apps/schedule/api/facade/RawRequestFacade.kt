package nl.klrnbk.daan.appiecal.apps.schedule.api.facade

import nl.klrnbk.daan.appiecal.apps.schedule.api.models.schedule.ScheduleResponse
import nl.klrnbk.daan.appiecal.apps.schedule.api.service.GqlService
import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.schedule.GqlScheduleResponseSchedule
import nl.klrnbk.daan.appiecal.apps.schedule.constants.DATE_TIME_FORMATTER
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class RawRequestFacade(
    private val gqlService: GqlService,
) {
    fun getRawSchedule(
        token: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): List<GqlScheduleResponseSchedule> {
        val startDateString = startDate.format(DATE_TIME_FORMATTER)
        val endDateString = endDate.format(DATE_TIME_FORMATTER)

        return gqlService.getRawSchedule(token, startDateString, endDateString)
    }

    fun getFormattedSchedule(
        token: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): ScheduleResponse {
        val startDateString = startDate.format(DATE_TIME_FORMATTER)
        val endDateString = endDate.format(DATE_TIME_FORMATTER)

        val mergedShifts = gqlService.getFetchedShiftsWithActivities(token, startDateString, endDateString)
        return ScheduleResponse(mergedShifts)
    }
}
