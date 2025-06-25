package nl.klrnbk.daan.appiecal.apps.schedule.api.models.schedule

import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.schedule.GqlScheduleResponseSchedule
import nl.klrnbk.daan.appiecal.apps.schedule.constants.DATE_TIME_FORMATTER
import nl.klrnbk.daan.appiecal.apps.schedule.constants.ShiftDepartment
import nl.klrnbk.daan.appiecal.apps.schedule.datasource.models.ShiftModel
import java.time.LocalDateTime
import java.util.UUID

data class ScheduleShift(
    val id: UUID,
    val storeId: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val departments: List<ShiftDepartment>,
    val activities: MutableList<ScheduleActivity>,
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
                startDate = LocalDateTime.parse(response.startTime, DATE_TIME_FORMATTER),
                endDate = LocalDateTime.parse(response.endTime, DATE_TIME_FORMATTER),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                departments = response.teamNames.map(ShiftDepartment::getFromGqlResponse),
                activities = mutableListOf(),
            )
    }
}
