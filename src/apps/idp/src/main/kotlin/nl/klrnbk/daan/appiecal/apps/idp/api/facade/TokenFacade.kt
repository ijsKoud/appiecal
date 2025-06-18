package nl.klrnbk.daan.appiecal.apps.idp.api.facade

import nl.klrnbk.daan.appiecal.apps.idp.api.service.AzureEntraService
import nl.klrnbk.daan.appiecal.apps.idp.api.service.AzureEntraUserIdpLinkService
import nl.klrnbk.daan.appiecal.apps.idp.exceptions.RefreshTokenException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TokenFacade(
    private val azureEntraUserIdpLinkService: AzureEntraUserIdpLinkService,
    private val azureEntraService: AzureEntraService,
) {
    private val logger: Logger = LoggerFactory.getLogger(TokenFacade::class.java)

    fun getAccessToken(
        userId: String,
        forceRefresh: Boolean = false,
    ): String? {
        val linkDetails = azureEntraUserIdpLinkService.getLinkFromUserId(userId)
        if (linkDetails == null) {
            if (forceRefresh) throw RefreshTokenException("No refresh token available for user $userId")
            return null
        }

        if (!linkDetails.isExpired() && !forceRefresh) return linkDetails.accessToken
        logger.info(
            "Refreshing access-token for user=$userId; original-expiration-date=${linkDetails.expirationDate}; forced=$forceRefresh",
        )

        val updatedToken = azureEntraService.getAccessTokenFromRefreshToken(linkDetails.refreshToken)
        azureEntraUserIdpLinkService.createOrReplaceLink(userId, updatedToken)

        return updatedToken.accessToken
    }
}
