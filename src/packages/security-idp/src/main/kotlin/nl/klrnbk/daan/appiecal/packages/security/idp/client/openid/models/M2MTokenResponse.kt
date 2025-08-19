package nl.klrnbk.daan.appiecal.packages.security.idp.client.openid.models

import com.google.gson.annotations.SerializedName

data class M2MTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String? = null,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("id_token") val idToken: String,
)
