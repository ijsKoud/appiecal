package nl.klrnbk.daan.appiecal.apps.calendar.api.facade

import nl.klrnbk.daan.appiecal.apps.calendar.api.service.CaldavService
import nl.klrnbk.daan.appiecal.apps.calendar.api.service.CalendarCredentialsService
import nl.klrnbk.daan.appiecal.apps.calendar.exceptions.MissingDavCredentialsException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EventFacade(
    private val caldavService: CaldavService,
    private val calendarCredentialsService: CalendarCredentialsService,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun deleteEvent(
        userId: String,
        eventId: String,
    ) {
        val credentials = calendarCredentialsService.getCredentials(userId)
        if (credentials == null || credentials.urls.calendarUrl == null) throw MissingDavCredentialsException()

        logger.info("Deleting event for user=$userId;event=$eventId")
        caldavService.deleteEvent(
            credentials.urls.calendarUrl,
            credentials.authentication.scope,
            credentials.authentication.token,
            eventId,
        )
    }
}
