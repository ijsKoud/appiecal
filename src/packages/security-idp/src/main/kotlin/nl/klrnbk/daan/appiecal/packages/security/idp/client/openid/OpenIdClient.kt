package nl.klrnbk.daan.appiecal.packages.security.idp.client.openid

import nl.klrnbk.daan.appiecal.packages.common.exceptions.ApiException
import nl.klrnbk.daan.appiecal.packages.common.retrofit.RetrofitClient
import nl.klrnbk.daan.appiecal.packages.security.idp.client.openid.models.M2MTokenResponse
import nl.klrnbk.daan.appiecal.packages.security.idp.client.openid.models.OpenIdJwksResponse
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
@EnableConfigurationProperties(OpenIdClientProperties::class)
class OpenIdClient(
    val properties: OpenIdClientProperties,
) : RetrofitClient() {
    private val apiClient = getRetrofitClient(OpenIdApiInterface::class.java, properties.jwks.uri)

    fun getFreshJwks(): OpenIdJwksResponse {
        val apiCall = apiClient.getJwks(properties.jwks.uri)
        return handleApiCall(apiCall)
    }

    fun getM2MToken(): M2MTokenResponse {
        val m2mConfig = properties.m2m
        if (m2mConfig ==
            null
        ) {
            throw ApiException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "M2M config required to fetch token",
                "No M2M configuration provided, please check the application.yml file",
            )
        }

        val query =
            m2mConfig.url
                .toHttpUrl()
                .newBuilder()
                .setQueryParameter("client_id", m2mConfig.clientId)
                .setQueryParameter("username", m2mConfig.username)
                .setQueryParameter("password", m2mConfig.password)
                .setQueryParameter("scope", m2mConfig.scope)
                .setQueryParameter("grant_type", "client_credentials")
                .build()
                .encodedQuery
                .orEmpty()

        val apiCall =
            apiClient.getM2MToken(
                m2mConfig.url,
                query,
            )

        return handleApiCall(apiCall)
    }
}
