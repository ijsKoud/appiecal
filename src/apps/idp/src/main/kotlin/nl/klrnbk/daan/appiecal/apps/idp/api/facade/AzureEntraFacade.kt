package nl.klrnbk.daan.appiecal.apps.idp.api.facade

import nl.klrnbk.daan.appiecal.apps.idp.api.service.AzureEntraService
import org.springframework.stereotype.Service

@Service
class AzureEntraFacade(
    private val azureEntraService: AzureEntraService,
) {
    fun getAzureEntraUrl(): String = azureEntraService.getAzureEntraUrl()

    fun linkUserWithAzureEntra(authorizationCode: String): String {
        val token = azureEntraService.authorizeWithCode(authorizationCode)
        return token
    }
}
