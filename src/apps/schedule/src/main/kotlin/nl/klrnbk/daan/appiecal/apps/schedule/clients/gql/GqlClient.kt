package nl.klrnbk.daan.appiecal.apps.schedule.clients.gql

import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.activity.GqlActivityBody
import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.activity.GqlActivityResponse
import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.schedule.GqlScheduleBody
import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.schedule.GqlScheduleResponse
import nl.klrnbk.daan.appiecal.packages.common.retrofit.RetrofitClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class GqlClient(
    @Value("\${appiecal.schedule.gql.url}") baseUrl: String,
) : RetrofitClient() {
    private val apiClient = getRetrofitClient(GqlApiInterface::class.java, baseUrl)

    fun getSchedule(
        token: String,
        startDate: String,
        endDate: String,
    ): GqlScheduleResponse {
        val call = apiClient.getSchedule("Bearer $token", GqlScheduleBody(startDate, endDate))
        return handleApiCall(call)
    }

    fun getScheduleActivities(
        token: String,
        startDate: String,
        endDate: String,
    ): GqlActivityResponse {
        val call = apiClient.getActivities("Bearer $token", GqlActivityBody(startDate, endDate))
        return handleApiCall(call)
    }
}
