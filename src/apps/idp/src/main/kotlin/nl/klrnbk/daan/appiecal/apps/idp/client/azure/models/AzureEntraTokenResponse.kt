package nl.klrnbk.daan.appiecal.apps.idp.client.azure.models

import com.google.gson.annotations.SerializedName

data class AzureEntraTokenResponse(
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("ext_expires_in") val extExpiresIn: Int,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("id_token") val idToken: String,
    val scope: String,
)
