package nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.schedule

class GqlScheduleBody(
    startDate: String,
    endDate: String,
) {
    val operationName: String = "scheduleOverview"
    val variables = mapOf<String, Any>("startDate" to startDate, "endDate" to endDate)
    val query: String =
        """
        query scheduleOverview(${"$"}startDate: String!, ${"$"}endDate: String!) {
            scheduleOverview(startDate: ${"$"}startDate, endDate: ${"$"}endDate) {
                shifts {
                    startTime
                    endTime
                    minutes
                    storeId
                    leaveMinutes
                    store {
                        abbreviatedDisplayName
                        location
                        id
                        __typename
                    }
                    paidMinutes
                    sickMinutes
                    teamNames
                    __typename
                }
            }
        }
        """.trimIndent()
}
