package nl.klrnbk.daan.appiecal.apps.schedule.api.service

import nl.klrnbk.daan.appiecal.apps.schedule.api.models.schedule.ScheduleActivity
import nl.klrnbk.daan.appiecal.apps.schedule.api.models.schedule.ScheduleShift
import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.GqlClient
import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.schedule.GqlScheduleResponseSchedule
import nl.klrnbk.daan.appiecal.apps.schedule.helpers.getUniqueStoreIds
import nl.klrnbk.daan.appiecal.apps.schedule.helpers.isActivityPartOfShift
import org.springframework.stereotype.Service

@Service
class GqlService(
    val gqlClient: GqlClient,
) {
    fun getRawSchedule(
        token: String,
        startDate: String,
        endDate: String,
    ): List<GqlScheduleResponseSchedule> {
        val response = gqlClient.getSchedule(token, startDate, endDate)
        return response.data.scheduleOverview?.shifts ?: emptyList()
    }

    fun getFetchedShifts(
        token: String,
        startDate: String,
        endDate: String,
    ): List<ScheduleShift> {
        val rawShifts = gqlClient.getSchedule(token, startDate, endDate)
        return rawShifts.data.scheduleOverview
            ?.shifts
            ?.map(ScheduleShift::fromGqlResponse) ?: emptyList()
    }

    fun getFetchedActivities(
        token: String,
        storeId: String,
        startDate: String,
        endDate: String,
    ): List<ScheduleActivity> {
        val rawActivities = gqlClient.getScheduleActivities(token, storeId, startDate, endDate)
        return rawActivities.data.scheduleByFilter?.map(ScheduleActivity::fromGqlResponse) ?: emptyList()
    }

    fun getFetchedShiftsWithActivities(
        token: String,
        startDate: String,
        endDate: String,
    ): List<ScheduleShift> {
        val shifts = getFetchedShifts(token, startDate, endDate)
        val activities =
            getUniqueStoreIds(shifts)
                .map {
                    getFetchedActivities(
                        token,
                        it,
                        startDate,
                        endDate,
                    )
                }.flatten()

        return mergeActivitiesWithShifts(shifts, activities)
    }

    fun mergeActivitiesWithShifts(
        shifts: List<ScheduleShift>,
        activities: List<ScheduleActivity>,
    ): List<ScheduleShift> {
        return shifts.map { shift ->
            val shiftActivities =
                activities
                    .filter { activity -> isActivityPartOfShift(shift, activity) }

            shift.activities.addAll(shiftActivities)
            return@map shift
        }
    }
}
