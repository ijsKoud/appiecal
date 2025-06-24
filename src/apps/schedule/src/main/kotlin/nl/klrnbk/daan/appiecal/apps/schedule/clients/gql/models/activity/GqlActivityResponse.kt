package nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.activity

data class GqlActivityResponse(
    val data: GqlActivityResponseDataObject,
)

data class GqlActivityResponseDataObject(
    val scheduleByFilter: List<GqlActivityResponseActivity>?,
)

data class GqlActivityResponseActivity(
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
