package nl.klrnbk.daan.appiecal.apps.idp.api.service

import nl.klrnbk.daan.appiecal.apps.idp.api.models.AzureEntraTokenDetails
import nl.klrnbk.daan.appiecal.apps.idp.config.IdpEncryptionConfig
import nl.klrnbk.daan.appiecal.apps.idp.datasource.models.AzureEntraUserIdpLinkModel
import nl.klrnbk.daan.appiecal.apps.idp.datasource.repositories.AzureEntraUserIdpLinkRepository
import nl.klrnbk.daan.appiecal.apps.idp.helpers.EncryptionHelper
import nl.klrnbk.daan.appiecal.packages.common.exceptions.JpaException
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
@EnableConfigurationProperties(IdpEncryptionConfig::class)
class AzureEntraUserIdpLinkService(
    private val repository: AzureEntraUserIdpLinkRepository,
    encryptionConfig: IdpEncryptionConfig,
) {
    private val encryptionHelper = EncryptionHelper(encryptionConfig)

    fun createOrReplaceLink(
        userId: String,
        token: AzureEntraTokenDetails,
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

    fun getLinkFromUserId(userId: String): AzureEntraTokenDetails? {
        val response = repository.findById(userId)
        val linkEntity = response.getOrNull()

        if (linkEntity == null) return null
        return AzureEntraTokenDetails.fromDatasource(linkEntity, encryptionHelper)
    }
}
