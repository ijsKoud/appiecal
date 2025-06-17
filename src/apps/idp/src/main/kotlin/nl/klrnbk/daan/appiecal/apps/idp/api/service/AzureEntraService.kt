package nl.klrnbk.daan.appiecal.apps.idp.api.service

import nl.klrnbk.daan.appiecal.apps.idp.client.azure.AzureEntraClient
import nl.klrnbk.daan.appiecal.apps.idp.client.azure.models.AzureEntraTokenResponse
import org.springframework.stereotype.Service

@Service
class AzureEntraService(
    private val azureEntraClient: AzureEntraClient,
) {
    fun getAzureEntraUrl(): String = azureEntraClient.config.authenticationUrl

    fun authorizeWithCode(code: String): AzureEntraTokenResponse {
        val token = azureEntraClient.authorizeWithCode(code)
        return token
    }
}
