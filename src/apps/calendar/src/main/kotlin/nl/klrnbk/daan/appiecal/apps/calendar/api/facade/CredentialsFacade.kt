package nl.klrnbk.daan.appiecal.apps.calendar.api.facade

import nl.klrnbk.daan.appiecal.apps.calendar.api.service.CaldavService
import nl.klrnbk.daan.appiecal.apps.calendar.api.service.CalendarCredentialsService
import nl.klrnbk.daan.appiecal.packages.common.shared.LinkStatus
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class CredentialsFacade(
    private val calendarCredentialsService: CalendarCredentialsService,
    private val caldavService: CaldavService,
) {
    private val logger = LoggerFactory.getLogger(CredentialsFacade::class.java)

    fun linkCaldavToUser(
        userId: String,
        baseUrl: String,
        authScope: String,
        authToken: String,
    ): ResponseEntity<Unit> {
        val principalUrl = caldavService.getPrincipal(baseUrl, authScope, authToken)
        val calendarHomeSetUrl = caldavService.getCalendarHomeSet(principalUrl, authScope, authToken)

        calendarCredentialsService.saveCredentials(userId, authScope, authToken, baseUrl, principalUrl, calendarHomeSetUrl, null)
        logger.info("Linking caldav server/credentials to user=$userId;base-url=$baseUrl")

        return ResponseEntity.noContent().build()
    }

    fun unlinkUser(userId: String): ResponseEntity<Unit> {
        calendarCredentialsService.removeCredentials(userId)
        logger.info("Unlinking caldav server/credentials from user=$userId;")

        return ResponseEntity.noContent().build()
    }

    fun getLinkStatus(userId: String): String {
        val isLinked = calendarCredentialsService.doesLinkExistForUser(userId)
        return LinkStatus.fromBoolean(isLinked).status
    }
}
