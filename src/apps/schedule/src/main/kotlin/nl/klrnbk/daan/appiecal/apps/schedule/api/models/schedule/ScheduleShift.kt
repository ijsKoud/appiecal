package nl.klrnbk.daan.appiecal.apps.schedule.api.models.schedule

import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.schedule.GqlScheduleResponseSchedule
import nl.klrnbk.daan.appiecal.apps.schedule.datasource.models.ShiftModel
import nl.klrnbk.daan.appiecal.apps.schedule.helpers.localDateTimeStringToZonedDateTime
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.ShiftDepartment
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponseShift
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.collections.map

class ScheduleShift(
    id: UUID,
    storeId: String,
    startDate: ZonedDateTime,
    endDate: ZonedDateTime,
    createdAt: ZonedDateTime,
    updatedAt: ZonedDateTime,
    departments: List<ShiftDepartment>,
    activities: MutableList<ScheduleActivity>,
) : ScheduleResponseShift<ScheduleActivity>(
        id,
        storeId,
        startDate,
        endDate,
        createdAt,
        updatedAt,
        departments,
        activities,
    ) {
    companion object {
        fun fromModel(model: ShiftModel): ScheduleShift =
            ScheduleShift(
                id = UUID.fromString(model.id),
                storeId = model.storeId,
                startDate = model.startDate,
                endDate = model.endDate,
                createdAt = model.createdAt,
                updatedAt = model.updatedAt,
                departments = model.departments,
                activities = model.activities.map(ScheduleActivity.Companion::fromModel).toMutableList(),
            )

        fun fromGqlResponse(response: GqlScheduleResponseSchedule): ScheduleShift =
            ScheduleShift(
                id = UUID.randomUUID(),
                storeId = response.storeId,
                startDate = localDateTimeStringToZonedDateTime(response.startTime),
                endDate = localDateTimeStringToZonedDateTime(response.endTime),
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                departments = response.teamNames.map(ShiftDepartment::getFromGqlResponse),
                activities = mutableListOf(),
            )
    }
}
