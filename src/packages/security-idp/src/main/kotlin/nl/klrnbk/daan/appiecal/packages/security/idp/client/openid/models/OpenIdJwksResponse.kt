package nl.klrnbk.daan.appiecal.packages.security.idp.client.openid.models

import com.google.gson.annotations.SerializedName

data class OpenIdJwksResponse(
    @SerializedName("keys") var keys: ArrayList<OpenIdJwksResponseKeys> = arrayListOf(),
)

data class OpenIdJwksResponseKeys(
    @SerializedName("alg") var alg: String? = null,
    @SerializedName("kid") var kid: String? = null,
    @SerializedName("kty") var kty: String? = null,
    @SerializedName("use") var use: String? = null,
    @SerializedName("n") var n: String? = null,
    @SerializedName("e") var e: String? = null,
    @SerializedName("x5c") var x5c: ArrayList<String> = arrayListOf(),
    @SerializedName("x5t") var x5t: String? = null,
    @SerializedName("x5t#S256") var x5tS256: String? = null,
)
