package nl.klrnbk.daan.appiecal.apps.idp.client.azure

import nl.klrnbk.daan.appiecal.apps.idp.client.azure.models.AzureEntraTokenResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface AzureEntraApiInterface {
    @FormUrlEncoded
    @POST("/oauth2/v2.0/authorize")
    @Headers("accept: application/json")
    fun authorizeWithCode(
        @Field("client_id") clientId: String,
        @Field("scope") scope: String,
        @Field("grant_type") grantType: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("code") code: String,
    ): Call<AzureEntraTokenResponse>

    @FormUrlEncoded
    @POST("/oauth2/v2.0/token")
    fun getAccessTokenFromRefreshToken(
        @Field("client_id") clientId: String,
        @Field("scope") scope: String,
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String = "access_token",
    ): Call<AzureEntraTokenResponse>
}
