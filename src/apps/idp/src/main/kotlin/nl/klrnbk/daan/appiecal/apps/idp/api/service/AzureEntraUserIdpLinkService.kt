package nl.klrnbk.daan.appiecal.apps.idp.api.service

import nl.klrnbk.daan.appiecal.apps.idp.api.models.AzureEntraToken
import nl.klrnbk.daan.appiecal.apps.idp.datasource.models.AzureEntraUserIdpLinkModel
import nl.klrnbk.daan.appiecal.apps.idp.datasource.repositories.AzureEntraUserIdpLinkRepository
import nl.klrnbk.daan.appiecal.apps.idp.helpers.EncryptionHelper
import nl.klrnbk.daan.appiecal.packages.common.exceptions.JpaException
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class AzureEntraUserIdpLinkService(
    private val repository: AzureEntraUserIdpLinkRepository,
    private val encryptionHelper: EncryptionHelper,
) {
    fun createOrReplaceLink(
        userId: String,
        token: AzureEntraToken,
    ) {
        val encryptedAccessToken = encryptionHelper.encryptStr(token.accessToken)
        val encryptedRefreshToken = encryptionHelper.encryptStr(token.refreshToken)

        val linkEntity =
            AzureEntraUserIdpLinkModel(
                id = userId,
                accessToken = encryptedAccessToken,
                refreshToken = encryptedRefreshToken,
                expirationDate = token.expirationDate,
            )

        try {
            repository.save(linkEntity)
        } catch (e: Exception) {
            throw JpaException(e.message ?: "Unable to save 'AzureEntraUserIdpLink'")
        }
    }

    fun getLinkFromUserId(userId: String): AzureEntraToken? {
        val response = repository.findById(userId)
        val linkEntity = response.getOrNull()

        if (linkEntity == null) return null
        return AzureEntraToken.fromDatasource(linkEntity, encryptionHelper)
    }
}
