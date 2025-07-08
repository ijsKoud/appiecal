package nl.klrnbk.daan.appiecal.apps.calendar.api.service

import nl.klrnbk.daan.appiecal.apps.calendar.api.models.CalendarCredentialsDetails
import nl.klrnbk.daan.appiecal.apps.calendar.datasource.models.CalendarCredentialsModel
import nl.klrnbk.daan.appiecal.apps.calendar.datasource.repositories.CalendarCredentialsRepository
import nl.klrnbk.daan.appiecal.packages.security.idp.helpers.EncryptionHelper
import okhttp3.HttpUrl.Companion.toHttpUrl
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

    fun getCredentials(userId: String): CalendarCredentialsDetails? {
        val entities = calendarCredentialsRepository.findByUserId(userId)
        val entity = entities.firstOrNull()
        if (entity == null) return null

        val responseModel = CalendarCredentialsDetails.fromModel(entity, encryptionHelper)
        return responseModel
    }

    fun setOrUpdateCalendarUrl(
        userId: String,
        href: String?,
    ): CalendarCredentialsDetails? {
        val entities = calendarCredentialsRepository.findByUserId(userId)
        val entity = entities.firstOrNull()
        if (entity == null) return null

        val urlBuilder = entity.calendarHomeSetUrl.toHttpUrl()
        val url =
            if (href == null) {
                null
            } else {
                urlBuilder
                    .resolve(href)
                    .toString()
            }

        val updatedEntity = entity.copy(calendarUrl = url)
        calendarCredentialsRepository.save(updatedEntity)

        val responseModel = CalendarCredentialsDetails.fromModel(entity, encryptionHelper)
        return responseModel
    }

    fun doesLinkExistForUser(userId: String): Boolean = calendarCredentialsRepository.existsByUserId(userId)
}
