package nl.klrnbk.daan.appiecal.apps.idp.api.service

import nl.klrnbk.daan.appiecal.apps.idp.client.azure.AzureEntraClient
import org.springframework.stereotype.Service

@Service
class AzureEntraService(
    private val azureEntraConfigClient: AzureEntraClient,
) {
    fun getAzureEntraUrl(): String = azureEntraConfigClient.config.authenticationUrl
}
