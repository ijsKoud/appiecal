package nl.klrnbk.daan.appiecal.apps.schedule.clients.idp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface IdpApiInterface {
    @GET("/v1/token/internal/access-token/{userId}")
    fun getAccessToken(
        @Header("X-Authorization") token: String,
        @Path("userId") userId: String,
    ): Call<String>
}
