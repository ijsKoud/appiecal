package nl.klrnbk.daan.appiecal.packages.security.idp.client.openid

import nl.klrnbk.daan.appiecal.packages.common.exceptions.ApiException
import nl.klrnbk.daan.appiecal.packages.common.exceptions.DownstreamServiceErrorException
import nl.klrnbk.daan.appiecal.packages.security.idp.client.openid.models.OpenIdJwksResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Component
@EnableConfigurationProperties(OpenIdClientProperties::class)
class OpenIdClient(
    val properties: OpenIdClientProperties,
) {
    private val apiClient = getRetrofitClient(properties.jwks.uri)

    fun getFreshJwks(): OpenIdJwksResponse {
        val apiCall = apiClient.getJwks(properties.jwks.uri)
        return handleApiCall(apiCall)
    }

    private fun <T> handleApiCall(call: Call<T>): T {
        val response = call.execute()
        if (!response.isSuccessful) {
            val errorStatus = response.code()
            throw DownstreamServiceErrorException(errorStatus, "Downstream service error")
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
        fun getRetrofitClient(baseUrl: String): OpenIdApiInterface {
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

            return retrofit.create(OpenIdApiInterface::class.java)
        }
    }
}
