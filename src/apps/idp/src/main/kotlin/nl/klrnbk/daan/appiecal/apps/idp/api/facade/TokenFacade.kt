package nl.klrnbk.daan.appiecal.apps.idp.api.facade

import nl.klrnbk.daan.appiecal.apps.idp.api.service.AzureEntraService
import nl.klrnbk.daan.appiecal.apps.idp.api.service.AzureEntraUserIdpLinkService
import org.springframework.stereotype.Service

@Service
class TokenFacade(
    private val azureEntraUserIdpLinkService: AzureEntraUserIdpLinkService,
    private val azureEntraService: AzureEntraService,
) {
    fun getAccessToken(userId: String): String? {
        val linkDetails = azureEntraUserIdpLinkService.getLinkFromUserId(userId)
        if (linkDetails == null) return null

        if (!linkDetails.isExpired()) return linkDetails.accessToken
        val updatedToken = azureEntraService.getAccessTokenFromRefreshToken(linkDetails.refreshToken)

        azureEntraUserIdpLinkService.createOrReplaceLink(userId, updatedToken)
        return updatedToken.accessToken
    }
}
