package nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.schedule

class GqlScheduleResponse {
    class DataObject(
        val scheduleOverview: Overview,
    )

    class Overview(
        val shifts: List<Schedule>,
        val latestScheduleDate: String,
    )

    data class Schedule(
        val startTime: String,
        val endTime: String,
        val minutes: Int,
        val storeId: String,
        val leaveMinutes: Int,
        val paidMinutes: Int,
        val sickMinutes: Int,
        val teamNames: List<String>,
    )

    val data: DataObject? = null
}
