package nl.klrnbk.daan.appiecal.apps.schedule.api.models.schedule

import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.activity.GqlActivityResponseActivity
import nl.klrnbk.daan.appiecal.apps.schedule.constants.DATE_TIME_FORMATTER
import nl.klrnbk.daan.appiecal.apps.schedule.constants.ShiftDepartment
import nl.klrnbk.daan.appiecal.apps.schedule.datasource.models.ActivityModel
import java.time.LocalDateTime
import java.util.UUID

data class ScheduleActivity(
    val id: UUID,
    val description: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
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
                department = model.department,
                timeCode = model.timeCode,
                paid = model.paid,
            )

        fun fromGqlResponse(response: GqlActivityResponseActivity): ScheduleActivity =
            ScheduleActivity(
                id = UUID.randomUUID(),
                description = response.description,
                startDate = LocalDateTime.parse(response.startTime, DATE_TIME_FORMATTER),
                endDate = LocalDateTime.parse(response.endTime, DATE_TIME_FORMATTER),
                department = ShiftDepartment.getFromGqlResponse(response.teamName),
                timeCode = response.timeCode,
                paid = !response.unpaid,
            )
    }
}
