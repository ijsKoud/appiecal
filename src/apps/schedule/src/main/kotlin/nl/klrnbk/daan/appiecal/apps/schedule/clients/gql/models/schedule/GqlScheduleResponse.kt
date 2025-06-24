package nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.schedule

class GqlScheduleResponse(
    val data: GqlScheduleResponseDataObject,
)

class GqlScheduleResponseDataObject(
    val scheduleOverview: GqlScheduleResponseOverview?,
)

class GqlScheduleResponseOverview(
    val shifts: List<GqlScheduleResponseSchedule>,
    val latestScheduleDate: String,
)

data class GqlScheduleResponseSchedule(
    val startTime: String,
    val endTime: String,
    val minutes: Int,
    val storeId: String,
    val leaveMinutes: Int,
    val paidMinutes: Int,
    val sickMinutes: Int,
    val teamNames: List<String>,
)
