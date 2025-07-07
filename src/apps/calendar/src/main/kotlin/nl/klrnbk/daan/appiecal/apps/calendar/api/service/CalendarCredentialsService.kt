package nl.klrnbk.daan.appiecal.apps.calendar.api.service

import nl.klrnbk.daan.appiecal.apps.calendar.datasource.models.CalendarCredentialsModel
import nl.klrnbk.daan.appiecal.apps.calendar.datasource.repositories.CalendarCredentialsRepository
import nl.klrnbk.daan.appiecal.packages.security.idp.helpers.EncryptionHelper
import org.springframework.stereotype.Service

@Service
class CalendarCredentialsService(
    private val calendarCredentialsRepository: CalendarCredentialsRepository,
    private val encryptionHelper: EncryptionHelper,
) {
    fun saveCredentials(
        userId: String,
        authScope: String,
        authToken: String,
        baseUrl: String,
        principalUrl: String,
        calendarHomeSetUrl: String,
        calendarUrl: String?,
    ): CalendarCredentialsModel {
        val encryptedAuthToken = encryptionHelper.encryptStr(authToken)
        val model =
            CalendarCredentialsModel(
                null,
                userId,
                authScope,
                encryptedAuthToken,
                baseUrl,
                principalUrl,
                calendarHomeSetUrl,
                calendarUrl,
            )

        return calendarCredentialsRepository.save(model)
    }

    fun removeCredentials(userId: String) {
        val entities = calendarCredentialsRepository.findByUserId(userId)
        if (entities.isEmpty()) return

        calendarCredentialsRepository.delete(entities.first())
    }
}
