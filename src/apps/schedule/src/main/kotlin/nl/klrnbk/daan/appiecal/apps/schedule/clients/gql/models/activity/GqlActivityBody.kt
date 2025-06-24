package nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.activity

class GqlActivityBody(
    storeId: String,
    startDate: String,
    endDate: String,
) {
    val operationName: String = "scheduleByFilter"
    val variables = mapOf("filter" to mapOf("storeId" to storeId), "startDate" to startDate, "endDate" to endDate)
    val query: String =
        """
        query scheduleByFilter(${"$"}filter: ActivityFilter!, ${"$"}startDate: String!, ${"$"}endDate: String!) {
            scheduleByFilter(filter: ${"$"}filter, startDate: ${"$"}startDate, endDate: ${"$"}endDate) {
                startTime
                endTime
                storeId
                category
                unpaid
                timeCode
                timeCodeName
                hourType
                minutes
                teamName
                description
            }
        }
        """.trimIndent()
}
