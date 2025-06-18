package nl.klrnbk.daan.appiecal.apps.idp.api.models

import nl.klrnbk.daan.appiecal.apps.idp.client.azure.models.AzureEntraTokenResponse
import nl.klrnbk.daan.appiecal.apps.idp.datasource.models.AzureEntraUserIdpLinkModel
import nl.klrnbk.daan.appiecal.apps.idp.helpers.EncryptionHelper
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class AzureEntraTokenDetails(
    val expirationDate: LocalDateTime,
    val refreshToken: String,
    val accessToken: String,
) {
    fun isExpired(): Boolean = LocalDateTime.now().isAfter(expirationDate)

    companion object {
        fun fromDatasource(
            model: AzureEntraUserIdpLinkModel,
            encryptionHelper: EncryptionHelper,
        ): AzureEntraTokenDetails {
            val refreshToken = encryptionHelper.decryptStr(model.refreshToken)
            val accessToken = encryptionHelper.decryptStr(model.accessToken)

            return AzureEntraTokenDetails(
                expirationDate = model.expirationDate,
                refreshToken = refreshToken,
                accessToken = accessToken,
            )
        }

        fun fromAzureEntraTokenResponse(response: AzureEntraTokenResponse): AzureEntraTokenDetails {
            val accessToken = response.accessToken
            val refreshToken = response.refreshToken

            val expirationDate = LocalDateTime.now()
            expirationDate.plus(response.expiresIn.toLong(), ChronoUnit.MILLIS)

            return AzureEntraTokenDetails(
                expirationDate = expirationDate,
                accessToken = accessToken,
                refreshToken = refreshToken,
            )
        }
    }
}
