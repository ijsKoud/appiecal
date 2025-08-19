package nl.klrnbk.daan.appiecal.apps.schedule.api.models.schedule

import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.activity.GqlActivityResponseActivity
import nl.klrnbk.daan.appiecal.apps.schedule.datasource.models.ActivityModel
import nl.klrnbk.daan.appiecal.apps.schedule.helpers.localDateTimeStringToZonedDateTime
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.ShiftDepartment
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponseShiftActivity
import java.time.ZonedDateTime
import java.util.UUID

class ScheduleActivity(
    id: UUID,
    description: String,
    startDate: ZonedDateTime,
    endDate: ZonedDateTime,
    createdAt: ZonedDateTime,
    updatedAt: ZonedDateTime,
    department: ShiftDepartment,
    timeCode: String,
    paid: Boolean,
) : ScheduleResponseShiftActivity(id, description, startDate, endDate, createdAt, updatedAt, department, timeCode, paid) {
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
