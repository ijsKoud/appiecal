package nl.klrnbk.daan.appiecal.packages.security.idp.client.openid

import nl.klrnbk.daan.appiecal.packages.security.idp.client.openid.models.M2MTokenResponse
import nl.klrnbk.daan.appiecal.packages.security.idp.client.openid.models.OpenIdJwksResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface OpenIdApiInterface {
    @GET
    @Headers("accept: application/json")
    fun getJwks(
        @Url url: String,
    ): Call<OpenIdJwksResponse>

    @POST
    @Headers("accept: application/json", "Content-Type: application/x-www-form-urlencoded")
    fun getM2MToken(
        @Url url: String,
        @Body credentials: String,
    ): Call<M2MTokenResponse>
}
