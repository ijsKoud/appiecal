package nl.klrnbk.daan.appiecal.apps.idp.api.service

import nl.klrnbk.daan.appiecal.apps.idp.client.azure.AzureEntraClient
import org.springframework.stereotype.Service

@Service
class AzureEntraService(
    private val azureEntraClient: AzureEntraClient,
) {
    fun getAzureEntraUrl(): String = azureEntraClient.config.authenticationUrl

    fun authorizeWithCode(code: String): String {
        val token = azureEntraClient.authorizeWithCode(code)
        return token.accessToken
    }
}
