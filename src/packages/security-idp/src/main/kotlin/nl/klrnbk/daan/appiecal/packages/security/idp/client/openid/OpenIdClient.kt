package nl.klrnbk.daan.appiecal.packages.security.idp.client.openid

import nl.klrnbk.daan.appiecal.packages.common.retrofit.RetrofitClient
import nl.klrnbk.daan.appiecal.packages.security.idp.client.openid.models.OpenIdJwksResponse
import org.springframework.boot.context.properties.EnableConfigurationProperties
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
}
