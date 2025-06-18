package nl.klrnbk.daan.appiecal.apps.idp.api.facade

import nl.klrnbk.daan.appiecal.apps.idp.api.models.AzureEntraTokenDetails
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

    fun getAccessToken(userId: String): String? {
        val tokenDetails = azureEntraUserIdpLinkService.getLinkFromUserId(userId)
        if (tokenDetails == null) return null
        if (!tokenDetails.isExpired()) return tokenDetails.accessToken

        val updatedTokenDetails = refreshAccessToken(userId, tokenDetails)
        return updatedTokenDetails.accessToken
    }

    fun forceRefreshAccessToken(userId: String): String {
        val tokenDetails = azureEntraUserIdpLinkService.getLinkFromUserId(userId)
        if (tokenDetails == null) throw RefreshTokenException("No refresh token available for user $userId")

        val refreshedTokenDetails = refreshAccessToken(userId, tokenDetails)
        return refreshedTokenDetails.accessToken
    }

    fun revokeTokens(userId: String) {
        logger.info("Revoking access+refresh-token for user=$userId")
        azureEntraUserIdpLinkService.deleteLinkByUserId(userId)
    }

    private fun refreshAccessToken(
        userId: String,
        tokenDetails: AzureEntraTokenDetails,
    ): AzureEntraTokenDetails {
        logger.info(
            "Refreshing access-token for user=$userId; original-expiration-date=${tokenDetails.expirationDate};",
        )

        val updatedToken = azureEntraService.getAccessTokenFromRefreshToken(tokenDetails.refreshToken)
        azureEntraUserIdpLinkService.createOrReplaceLink(userId, updatedToken)

        return updatedToken
    }
}
