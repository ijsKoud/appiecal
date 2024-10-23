package dev.ijskoud.appiecal.authentication

import com.google.gson.Gson
import dev.ijskoud.appiecal.authentication.interfaces.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response

/**
 * In charge of handling the Microsoft Authentication
 */
class AuthenticationRepository {
    companion object {
        const val BASE_URL = "https://login.microsoftonline.com/a6b169f1-592b-4329-8f33-8db8903003c7/"
        const val AUTHORIZE_URL = "oauth2/v2.0/authorize"
        const val TOKEN_URL = "oauth2/v2.0/token"
    }

    private val apiService: ApiService
    private val logger: Logger = LoggerFactory.getLogger(AuthenticationRepository::class.java)

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    /**
     * Returns the authorization url
     */
    fun getAuthorizationUrl(): String {
        return "$BASE_URL$AUTHORIZE_URL?scope=openid+profile+offline_access+https%3A%2F%2Fdex.ahold.com%2F.default&client_id=4d469dcc-1140-449f-856e-7d2618ec9c36&redirect_uri=http%3A%2F%2Flocalhost&response_type=code";
    }

    /**
     * Authenticates the user when signing in for the first time
     * @param code The authorization code given after signing in
     */
    fun getAuthorization(code: String): TokenResponse? {
        return try {
            logger.debug("Retrieving token information using grant_type=authorization_code")

            val call = apiService.getAuthorization(
                url = BASE_URL + TOKEN_URL,
                clientId = "4d469dcc-1140-449f-856e-7d2618ec9c36",
                scope = "openid profile offline_access https://dex.ahold.com/.default",
                grantType = "authorization_code",
                redirectUri = "http://localhost",
                code = code
            )

            val response: Response<TokenResponse> = call.execute()
            if (response.isSuccessful) {
                logger.debug("Action completed successfully with grant_type=authorization_code")
                response.body()
            } else {
                val error = Gson().fromJson(response.errorBody()?.charStream(), ApiError::class.java)
                logger.error("Action failed with grant_type=authorization_code, code=${response.code()}, error=${error.error_description}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            logger.error("Action failed with grant_type=authorization_code, error=" + e.localizedMessage)
            null
        }
    }

    /**
     * Generates a new authentication token using the refresh token
     * @param refreshToken The refresh token for generating a new authentication token
     */
    fun getNewToken(refreshToken: String): TokenResponse? {
        return try {
            logger.debug("Retrieving token information using grant_type=refresh_token")

            val call = apiService.getNewToken(
                url = BASE_URL + TOKEN_URL,
                clientId = "4d469dcc-1140-449f-856e-7d2618ec9c36",
                scope = "openid profile offline_access https://dex.ahold.com/.default",
                grantType = "refresh_token",
                refreshToken = refreshToken
            )
            val response: Response<TokenResponse> = call.execute()
            if (response.isSuccessful) {
                logger.debug("Action completed successfully with grant_type=refresh_token")
                response.body()
            } else {
                logger.error("Action failed with grant_type=refresh_token, code=${response.code()}, error=${response.errorBody().toString()}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

// Initial authentication URL:
// https://login.microsoftonline.com/a6b169f1-592b-4329-8f33-8db8903003c7/oauth2/v2.0/authorize?scope=openid+profile+offline_access+https%3A%2F%2Fdex.ahold.com%2F.default&client_id=4d469dcc-1140-449f-856e-7d2618ec9c36&redirect_uri=http%3A%2F%2Flocalhost&response_type=code