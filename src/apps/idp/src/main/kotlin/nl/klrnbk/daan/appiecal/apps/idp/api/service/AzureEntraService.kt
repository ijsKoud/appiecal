package nl.klrnbk.daan.appiecal.apps.idp.api.service

import nl.klrnbk.daan.appiecal.apps.idp.client.AzureEntraConfigClient
import org.springframework.stereotype.Service

@Service
class AzureEntraService(
    private val azureEntraConfigClient: AzureEntraConfigClient,
) {
    fun getAzureEntraUrl(): String = azureEntraConfigClient.config.authenticationUrl
}
