package nl.klrnbk.daan.appiecal.apps.idp.api.service

import nl.klrnbk.daan.appiecal.apps.idp.datasource.models.AzureEntraUserIdpLink
import nl.klrnbk.daan.appiecal.apps.idp.datasource.repositories.AzureEntraUserIdpLinkRepository
import nl.klrnbk.daan.appiecal.packages.common.exceptions.JpaException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AzureEntraUserIdpLinkService(
    private val repository: AzureEntraUserIdpLinkRepository,
) {
    fun createOrReplaceLink(
        userId: String,
        accessToken: String,
        refreshToken: String,
        expirationDate: LocalDateTime,
    ) {
        val linkEntity =
            AzureEntraUserIdpLink(
                id = userId,
                accessToken = accessToken,
                refreshToken = refreshToken,
                expirationDate = expirationDate,
            )

        try {
            repository.save(linkEntity)
        } catch (e: Exception) {
            throw JpaException(e.message ?: "Unable to save 'AzureEntraUserIdpLink'")
        }
    }
}
