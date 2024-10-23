package dev.ijskoud.appiecal.authentication.interfaces

data class TokenResponse(
    /** Authorization type (used for HTTP header) **/
    val token_type: String,

    /** Scopes the token is valid for **/
    val scope: String,

    val ext_expires_in: Int,

    /** The expiration time in seconds **/
    val expires_in: Int,

    /** The refresh token used to generate a new access token **/
    val refresh_token: String,

    /** The access token for the access to the API **/
    val access_token: String,

    /** Identification token (Unique to every session) **/
    val id_token: String
)