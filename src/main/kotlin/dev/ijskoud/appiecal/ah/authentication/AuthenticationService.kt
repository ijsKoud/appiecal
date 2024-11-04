package dev.ijskoud.appiecal.ah.authentication

import dev.ijskoud.appiecal.store.auth.AuthStore
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AuthenticationService {
    private val repository = AuthenticationRepository()
    private val store: AuthStore = AuthStore.getInstance()
    private val logger: Logger = LoggerFactory.getLogger(AuthenticationService::class.java)

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
     * Retrieves the access token from the store
     * @return Access token
     */
    fun getAccessToken(): String {
        val data = store.get()

        if (!data.isExpired) {
            logger.debug("Access token is still valid")
            return data.accessToken
        }

        val newToken = getNewToken(data.refreshToken)
        data.accessToken = newToken.accessToken
        data.refreshToken = newToken.refreshToken
        data.expireDate = newToken.expireDate

        store.save()
        logger.info("New access token acquired, valid until ${newToken.expireDate}")

        return data.accessToken
    }

    /**
     * Authenticates the user when signing in for the first time
     * @param code The authorization code given after signing in
     */
    fun getAuthorization(code: String): Token {
       val response = repository.getAuthorization(code) ?: throw Exception("Authorization error")
        return Token.create(response.refresh_token, response.access_token, response.expires_in.toLong())
    }

    /**
     * Generates a new authentication token using the refresh token
     * @param refreshToken The refresh token for generating a new authentication token
     */
    fun getNewToken(refreshToken: String): Token {
        val response = repository.getNewToken(refreshToken) ?: throw Exception("Authorization error")
        return Token.create(response.refresh_token, response.access_token, response.expires_in.toLong())
    }

    /**
     * Returns the authorization url
     */
    fun getAuthorizationUrl(): String {
        return repository.getAuthorizationUrl()
    }
}