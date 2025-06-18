package nl.klrnbk.daan.appiecal.apps.idp.api.facade

import nl.klrnbk.daan.appiecal.apps.idp.api.service.AzureEntraUserIdpLinkService
import org.springframework.stereotype.Service

@Service
class TokenFacade(
    private val azureEntraUserIdpLinkService: AzureEntraUserIdpLinkService,
) {
    fun getAccessToken(userId: String): String? {
        val linkDetails = azureEntraUserIdpLinkService.getLinkFromUserId(userId)
        return linkDetails?.accessToken
    }
}
