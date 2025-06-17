package nl.klrnbk.daan.appiecal.apps.idp.api.service

import nl.klrnbk.daan.appiecal.apps.idp.client.azure.AzureEntraClient
import nl.klrnbk.daan.appiecal.apps.idp.client.azure.models.AzureEntraTokenResponse
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.springframework.stereotype.Service

@Service
class AzureEntraService(
    private val azureEntraClient: AzureEntraClient,
) {
    fun getAzureEntraUrl(): String {
        val baseUrl = azureEntraClient.config.baseAuthenticationUrl
        val credentials = azureEntraClient.config.credentials

        val parsedBaseUrl = baseUrl.toHttpUrlOrNull()
        if (parsedBaseUrl == null) throw IllegalArgumentException("Expected valid base url but received invalid one; base-url: $baseUrl")

        val url =
            parsedBaseUrl
                .newBuilder()
                .addQueryParameter("client_id", credentials.clientId)
                .addQueryParameter("tenant_id", credentials.tenantId)
                .addQueryParameter("scope", credentials.scopes)
                .addQueryParameter("redirect_uri", credentials.redirectUri)
                .addQueryParameter("response_mode", credentials.responseMode)
                .addQueryParameter("response_type", credentials.responseType)
                .addQueryParameter("prompt", credentials.prompt)
                .build()

        return url.toString()
    }

    fun authorizeWithCode(code: String): AzureEntraTokenResponse {
        val token = azureEntraClient.authorizeWithCode(code)
        return token
    }
}
