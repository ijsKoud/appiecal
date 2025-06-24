package nl.klrnbk.daan.appiecal.apps.schedule.api.facade

import nl.klrnbk.daan.appiecal.apps.schedule.api.responses.schedule.ScheduleResponse
import nl.klrnbk.daan.appiecal.apps.schedule.api.service.GqlService
import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.schedule.GqlScheduleResponseSchedule
import nl.klrnbk.daan.appiecal.apps.schedule.constants.DATE_TIME_FORMATTER
import nl.klrnbk.daan.appiecal.apps.schedule.helpers.getUniqueStoreIds
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.text.format

@Service
class RawRequestFacade(
    val gqlService: GqlService,
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

        val shifts = gqlService.getFetchedShifts(token, startDateString, endDateString)
        val activities =
            getUniqueStoreIds(shifts)
                .map {
                    gqlService.getFetchedActivities(
                        token,
                        it,
                        startDateString,
                        endDateString,
                    )
                }.flatten()

        val mergedShifts = gqlService.mergeActivitiesWithShifts(shifts, activities)
        return ScheduleResponse(mergedShifts)
    }
}
