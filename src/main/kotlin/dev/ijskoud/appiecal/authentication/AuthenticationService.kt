package dev.ijskoud.appiecal.authentication

class AuthenticationService {
    private val repository = AuthenticationRepository()

    companion object {
        private var instance: AuthenticationService? = null

        /**
         * Gets instance (singleton)
         */
        fun getInstance(): AuthenticationService {
            if (instance == null) {
                instance = AuthenticationService()
            }

            return instance!!
        }
    }

    /**
     * Authenticates the user when signing in for the first time
     * @param code The authorization code given after signing in
     */
    fun getAuthorization(code: String): Token {
       val response = repository.getAuthorization(code) ?: throw Exception("Authorization error");
        return Token.create(response.refresh_token, response.access_token, response.expires_in.toLong())
    }

    /**
     * Generates a new authentication token using the refresh token
     * @param refreshToken The refresh token for generating a new authentication token
     */
    fun getNewToken(refreshToken: String): Token {
        val response = repository.getNewToken(refreshToken) ?: throw Exception("Authorization error");
        return Token.create(response.refresh_token, response.access_token, response.expires_in.toLong())
    }

    /**
     * Returns the authorization url
     */
    fun getAuthorizationUrl(): String {
        return repository.getAuthorizationUrl();
    }
}