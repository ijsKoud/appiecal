package nl.klrnbk.daan.appiecal.apps.schedule.clients.gql

import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.activity.GqlActivityBody
import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.activity.GqlActivityResponse
import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.schedule.GqlScheduleBody
import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.schedule.GqlScheduleResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GqlApiInterface {
    @POST("/external/ah/rtp/dex/graphql/")
    fun getSchedule(
        @Header("Authorization") token: String,
        @Body body: GqlScheduleBody,
    ): Call<GqlScheduleResponse>

    @POST("/external/ah/rtp/dex/graphql/")
    fun getActivities(
        @Header("Authorization") token: String,
        @Body body: GqlActivityBody,
    ): Call<GqlActivityResponse>
}
