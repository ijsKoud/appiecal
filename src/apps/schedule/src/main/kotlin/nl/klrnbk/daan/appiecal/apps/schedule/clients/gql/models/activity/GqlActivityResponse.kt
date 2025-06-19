package nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.activity

class GqlActivityResponse {
    data class DataObject(
        val scheduleByFilter: List<Activity>,
    )

    data class Activity(
        val startTime: String,
        val endTime: String,
        val minutes: Int,
        val storeId: String,
        val category: String,
        val unpaid: Boolean,
        val timeCode: String,
        val timeCodeName: String,
        val hourType: String,
        val teamName: String,
        val description: String,
    )

    val data: DataObject? = null
}
