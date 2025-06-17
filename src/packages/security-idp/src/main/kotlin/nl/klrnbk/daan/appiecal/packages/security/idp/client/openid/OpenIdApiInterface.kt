package nl.klrnbk.daan.appiecal.packages.security.idp.client.openid

import nl.klrnbk.daan.appiecal.packages.security.idp.client.openid.models.OpenIdJwksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface OpenIdApiInterface {
    @GET
    @Headers("accept: application/json")
    fun getJwks(
        @Url url: String,
    ): Call<OpenIdJwksResponse>
}
