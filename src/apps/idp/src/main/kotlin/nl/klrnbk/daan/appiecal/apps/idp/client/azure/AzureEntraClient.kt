package nl.klrnbk.daan.appiecal.apps.idp.client.azure

import nl.klrnbk.daan.appiecal.apps.idp.client.azure.models.AzureEntraTokenResponse
import nl.klrnbk.daan.appiecal.apps.idp.config.AzureEntraConfig
import nl.klrnbk.daan.appiecal.packages.common.retrofit.RetrofitClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component

@Component
@EnableConfigurationProperties(AzureEntraConfig::class)
class AzureEntraClient(
    val config: AzureEntraConfig,
) : RetrofitClient() {
    private val apiClient = getRetrofitClient(AzureEntraApiInterface::class.java, config.azureEntraUrl)

    fun authorizeWithCode(code: String): AzureEntraTokenResponse {
        val apiCall =
            apiClient.authorizeWithCode(
                config.credentials.tenantId,
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
                config.credentials.tenantId,
                config.credentials.clientId,
                config.credentials.scopes,
                refreshToken,
            )

        return handleApiCall(apiCall)
    }
}
