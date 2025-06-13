package nl.klrnbk.daan.appiecal.apps.idp.client.azure

import com.google.gson.Gson
import nl.klrnbk.daan.appiecal.apps.idp.client.azure.models.AzureEntraErrorResponse
import nl.klrnbk.daan.appiecal.apps.idp.client.azure.models.AzureEntraTokenResponse
import nl.klrnbk.daan.appiecal.apps.idp.config.AzureEntraConfig
import nl.klrnbk.daan.appiecal.packages.exceptions.models.ApiException
import nl.klrnbk.daan.appiecal.packages.exceptions.models.DownstreamServiceErrorException
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Component
@EnableConfigurationProperties(AzureEntraConfig::class)
class AzureEntraClient(
    val config: AzureEntraConfig,
) {
    private val apiClient = getRetrofitClient(config.azureEntraUrl)

    fun authorizeWithCode(code: String): AzureEntraTokenResponse {
        val apiCall =
            apiClient.authorizeWithCode(
                config.credentials.clientId,
                config.credentials.scopes,
                "authorization_code",
                "http://localhost",
                code,
            )

        return handleApiCall(apiCall)
    }

    fun getAccessTokenFromRefreshToken(refreshToken: String): AzureEntraTokenResponse {
        val apiCall =
            apiClient.getAccessTokenFromRefreshToken(
                config.credentials.clientId,
                config.credentials.scopes,
                refreshToken,
            )

        return handleApiCall(apiCall)
    }

    private fun <T> handleApiCall(call: Call<T>): T {
        val response = call.execute()
        if (!response.isSuccessful) {
            val error =
                Gson().fromJson(
                    response.errorBody()?.charStream(),
                    AzureEntraErrorResponse::class.java,
                )

            val errorStatus = response.code()
            throw DownstreamServiceErrorException(errorStatus, error.description)
        }

        val body = response.body()
        if (body == null) {
            throw ApiException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Downstream service returned malformed response body",
                "Empty or invalid response body returned",
            )
        }

        return body
    }

    companion object {
        fun getRetrofitClient(baseUrl: String): AzureEntraApiInterface {
            val loggingInterceptor =
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.NONE)

            val client =
                OkHttpClient
                    .Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()

            val retrofit =
                Retrofit
                    .Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()

            return retrofit.create(AzureEntraApiInterface::class.java)
        }
    }
}
