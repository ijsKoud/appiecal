package dev.ijskoud.appiecal.authentication.interfaces

import retrofit2.Call
import retrofit2.http.*

/**
 * Defines the possible API calls that retrofit can make
 */
interface ApiService {
    @FormUrlEncoded
    @POST
    @Headers("accept: application/json")
    fun getAuthorization(
        @Url url: String,
        @Field("client_id") clientId: String,
        @Field("scope") scope: String,
        @Field("grant_type") grantType: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("code") code: String
    ): Call<TokenResponse>

    @FormUrlEncoded
    @POST
    fun getNewToken(
        @Url url: String,
        @Field("client_id") clientId: String,
        @Field("scope") scope: String,
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String
    ): Call<TokenResponse>
}