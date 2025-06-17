package nl.klrnbk.daan.appiecal.apps.idp.api.facade

import nl.klrnbk.daan.appiecal.apps.idp.api.service.AzureEntraService
import nl.klrnbk.daan.appiecal.apps.idp.api.service.AzureEntraUserIdpLinkService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AzureEntraFacade(
    private val azureEntraService: AzureEntraService,
    private val azureEntraUserIdpLinkService: AzureEntraUserIdpLinkService,
) {
    fun getAzureEntraUrl(): String = azureEntraService.getAzureEntraUrl()

    fun linkUserWithAzureEntra(
        authorizationCode: String,
        userId: String,
    ) {
        val token = azureEntraService.authorizeWithCode(authorizationCode)
        azureEntraUserIdpLinkService.createOrReplaceLink(
            userId,
            token.accessToken,
            token.refreshToken,
            LocalDateTime.now(),
        )
    }
}
