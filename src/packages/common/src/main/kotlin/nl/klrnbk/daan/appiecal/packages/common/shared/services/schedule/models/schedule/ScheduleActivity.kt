package nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule

import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.activity.GqlActivityResponseActivity
import nl.klrnbk.daan.appiecal.apps.schedule.constants.ShiftDepartment
import nl.klrnbk.daan.appiecal.apps.schedule.datasource.models.ActivityModel
import nl.klrnbk.daan.appiecal.apps.schedule.helpers.localDateTimeStringToZonedDateTime
import java.time.ZonedDateTime
import java.util.UUID

data class ScheduleActivity(
    val id: UUID,
    val description: String,
    val startDate: ZonedDateTime,
    val endDate: ZonedDateTime,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val department: ShiftDepartment,
    val timeCode: String,
    val paid: Boolean,
) {
    companion object {
        fun fromModel(model: ActivityModel): ScheduleActivity =
            ScheduleActivity(
                id = UUID.fromString(model.id),
                description = model.description,
                startDate = model.startDate,
                endDate = model.endDate,
                createdAt = model.createdAt,
                updatedAt = model.updatedAt,
                department = model.department,
                timeCode = model.timeCode,
                paid = model.paid,
            )

        fun fromGqlResponse(response: GqlActivityResponseActivity): ScheduleActivity =
            ScheduleActivity(
                id = UUID.randomUUID(),
                description = response.description,
                startDate = localDateTimeStringToZonedDateTime(response.startTime),
                endDate = localDateTimeStringToZonedDateTime(response.endTime),
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                department = ShiftDepartment.getFromGqlResponse(response.teamName),
                timeCode = response.timeCode,
                paid = !response.unpaid,
            )
    }
}
