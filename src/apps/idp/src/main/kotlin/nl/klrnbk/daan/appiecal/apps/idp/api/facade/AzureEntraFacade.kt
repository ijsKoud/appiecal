package nl.klrnbk.daan.appiecal.apps.idp.api.facade

import nl.klrnbk.daan.appiecal.apps.idp.api.service.AzureEntraService
import nl.klrnbk.daan.appiecal.apps.idp.api.service.AzureEntraUserIdpLinkService
import nl.klrnbk.daan.appiecal.packages.common.shared.LinkStatus
import nl.klrnbk.daan.appiecal.packages.security.idp.models.JwtAuthenticationToken
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AzureEntraFacade(
    private val azureEntraService: AzureEntraService,
    private val azureEntraUserIdpLinkService: AzureEntraUserIdpLinkService,
) {
    private val logger: Logger = LoggerFactory.getLogger(AzureEntraFacade::class.java)

    fun getAzureEntraUrl(): String = azureEntraService.getAzureEntraUrl()

    fun getLinkStatus(authentication: JwtAuthenticationToken): String {
        val isLinked = azureEntraUserIdpLinkService.doesLinkExistForUser(authentication.principal)
        val status = LinkStatus.fromBoolean(isLinked)

        return status.status
    }

    fun linkUserWithAzureEntra(
        authorizationCode: String,
        userId: String,
    ) {
        logger.info("Linking Idp user with Azure Entra credentials; user=$userId")

        val token = azureEntraService.authorizeWithCode(authorizationCode)
        azureEntraUserIdpLinkService.createOrReplaceLink(
            userId,
            token,
        )
    }
}
