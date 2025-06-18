package nl.klrnbk.daan.appiecal.apps.idp.api.service

import nl.klrnbk.daan.appiecal.apps.idp.datasource.models.AzureEntraUserIdpLink
import nl.klrnbk.daan.appiecal.apps.idp.datasource.repositories.AzureEntraUserIdpLinkRepository
import nl.klrnbk.daan.appiecal.apps.idp.helpers.EncryptionHelper
import nl.klrnbk.daan.appiecal.packages.common.exceptions.JpaException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AzureEntraUserIdpLinkService(
    private val repository: AzureEntraUserIdpLinkRepository,
    private val encryptionHelper: EncryptionHelper,
) {
    fun createOrReplaceLink(
        userId: String,
        accessToken: String,
        refreshToken: String,
        expirationDate: LocalDateTime,
    ) {
        val encryptedAccessToken = encryptionHelper.encryptStr(accessToken)
        val encryptedRefreshToken = encryptionHelper.encryptStr(refreshToken)

        val linkEntity =
            AzureEntraUserIdpLink(
                id = userId,
                accessToken = encryptedAccessToken,
                refreshToken = encryptedRefreshToken,
                expirationDate = expirationDate,
            )

        try {
            repository.save(linkEntity)
        } catch (e: Exception) {
            throw JpaException(e.message ?: "Unable to save 'AzureEntraUserIdpLink'")
        }
    }
}
