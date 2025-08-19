package nl.klrnbk.daan.appiecal.apps.calendar.api.facade

import nl.klrnbk.daan.appiecal.apps.calendar.api.service.CaldavService
import nl.klrnbk.daan.appiecal.apps.calendar.api.service.CalendarCredentialsService
import nl.klrnbk.daan.appiecal.apps.calendar.api.service.IcalService
import nl.klrnbk.daan.appiecal.apps.calendar.exceptions.MissingDavCredentialsException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class EventFacade(
    private val caldavService: CaldavService,
    private val calendarCredentialsService: CalendarCredentialsService,
    private val icalService: IcalService,
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

    fun createEvent(
        userId: String,
        eventId: String,
        title: String,
        description: String,
        startDate: ZonedDateTime,
        endDate: ZonedDateTime,
    ): String {
        val credentials = calendarCredentialsService.getCredentials(userId)
        if (credentials == null || credentials.urls.calendarUrl == null) throw MissingDavCredentialsException()

        val calendar = icalService.createEvent(eventId, title, description, startDate, endDate)
        logger.info("Creating event for user=$userId;eventId=$eventId")

        caldavService.createEvent(
            credentials.urls.calendarUrl,
            credentials.authentication.scope,
            credentials.authentication.token,
            eventId,
            calendar,
        )

        return eventId
    }

    fun putEvent(
        userId: String,
        content: String,
    ): String {
        val credentials = calendarCredentialsService.getCredentials(userId)
        if (credentials == null || credentials.urls.calendarUrl == null) throw MissingDavCredentialsException()

        val (calendar, eventId) = icalService.getCalendarWithEventIdFromIcal(content)
        logger.info("Creating/updating event for user=$userId;eventId=$eventId")

        caldavService.createEvent(
            credentials.urls.calendarUrl,
            credentials.authentication.scope,
            credentials.authentication.token,
            eventId,
            calendar,
        )

        return eventId
    }
}
