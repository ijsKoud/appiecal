package nl.klrnbk.daan.appiecal.apps.calendar.api.models

import nl.klrnbk.daan.appiecal.apps.calendar.datasource.models.CalendarCredentialsModel
import nl.klrnbk.daan.appiecal.packages.security.idp.helpers.EncryptionHelper
import java.util.UUID

data class CalendarCredentialsDetails(
    val id: String,
    val userId: String,
    val authentication: CalendarCredentialsDetailsAuthentication,
    val urls: CalendarCredentialsDetailsUrls,
) {
    companion object {
        fun fromModel(
            model: CalendarCredentialsModel,
            encryptionHelper: EncryptionHelper,
        ): CalendarCredentialsDetails {
            val authentication = CalendarCredentialsDetailsAuthentication.fromModel(model, encryptionHelper)
            val urls = CalendarCredentialsDetailsUrls.fromModel(model)

            return CalendarCredentialsDetails(
                id = model.id ?: UUID.randomUUID().toString(),
                userId = model.userId,
                authentication = authentication,
                urls = urls,
            )
        }
    }
}

data class CalendarCredentialsDetailsAuthentication(
    val scope: String,
    val token: String,
) {
    companion object {
        fun fromModel(
            model: CalendarCredentialsModel,
            encryptionHelper: EncryptionHelper,
        ): CalendarCredentialsDetailsAuthentication {
            val unencryptedAuthToken = encryptionHelper.decryptStr(model.token)
            return CalendarCredentialsDetailsAuthentication(scope = model.scope, token = unencryptedAuthToken)
        }
    }
}

data class CalendarCredentialsDetailsUrls(
    val base: String,
    val principal: String,
    val calendarHomeSet: String,
    val calendarUrl: String?,
) {
    companion object {
        fun fromModel(model: CalendarCredentialsModel): CalendarCredentialsDetailsUrls =
            CalendarCredentialsDetailsUrls(
                base = model.baseUrl,
                principal = model.principalUrl,
                calendarHomeSet = model.calendarHomeSetUrl,
                calendarUrl = model.calendarUrl,
            )
    }
}
