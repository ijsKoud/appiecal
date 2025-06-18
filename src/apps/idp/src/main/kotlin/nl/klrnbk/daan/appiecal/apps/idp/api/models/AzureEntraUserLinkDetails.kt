package nl.klrnbk.daan.appiecal.apps.idp.api.models

import nl.klrnbk.daan.appiecal.apps.idp.datasource.models.AzureEntraUserIdpLinkModel
import nl.klrnbk.daan.appiecal.apps.idp.helpers.EncryptionHelper
import java.time.LocalDateTime

class AzureEntraUserLinkDetails(
    val expirationDate: LocalDateTime,
    val refreshToken: String,
    val accessToken: String,
) {
    fun isExpired(): Boolean = LocalDateTime.now().isAfter(expirationDate)

    companion object {
        fun fromDatasource(
            model: AzureEntraUserIdpLinkModel,
            encryptionHelper: EncryptionHelper,
        ): AzureEntraUserLinkDetails {
            val refreshToken = encryptionHelper.decryptStr(model.refreshToken)
            val accessToken = encryptionHelper.decryptStr(model.accessToken)

            return AzureEntraUserLinkDetails(
                expirationDate = model.expirationDate,
                refreshToken = refreshToken,
                accessToken = accessToken,
            )
        }
    }
}
