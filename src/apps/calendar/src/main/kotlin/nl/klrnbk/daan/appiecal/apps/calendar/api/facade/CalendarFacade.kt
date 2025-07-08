package nl.klrnbk.daan.appiecal.apps.calendar.api.facade

import nl.klrnbk.daan.appiecal.apps.calendar.api.models.CalendarListEntryResponse
import nl.klrnbk.daan.appiecal.apps.calendar.api.service.CaldavService
import nl.klrnbk.daan.appiecal.apps.calendar.api.service.CalendarCredentialsService
import nl.klrnbk.daan.appiecal.apps.calendar.exceptions.MissingDavCredentialsException
import nl.klrnbk.daan.appiecal.packages.common.exceptions.ApiException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class CalendarFacade(
    private val caldavService: CaldavService,
    private val calendarCredentialsService: CalendarCredentialsService,
) {
    fun getCalendarList(userId: String): List<CalendarListEntryResponse> {
        val credentials = calendarCredentialsService.getCredentials(userId)
        if (credentials == null) throw MissingDavCredentialsException()

        return caldavService.getCalendarList(
            credentials.urls.calendarHomeSet,
            credentials.authentication.scope,
            credentials.authentication.token,
        )
    }

    fun setOrUpdateCalendarUrl(
        userId: String,
        href: String?,
    ): ResponseEntity<Unit> {
        val calendarList = getCalendarList(userId)
        if (calendarList.isEmpty()) {
            throw ApiException(
                HttpStatus.BAD_REQUEST,
                "Unable to link the requested calendar",
                "CalDAV server returned empty list of valid calendars",
            )
        }

        if (href != null && !calendarList.any { it.href == href }) {
            throw ApiException(
                HttpStatus.BAD_REQUEST,
                "Unable to link the requested calendar",
                "Provided href cannot be found on the calDAV server",
            )
        }

        val result = calendarCredentialsService.setOrUpdateCalendarUrl(userId, href)
        if (result == null) throw MissingDavCredentialsException()

        return ResponseEntity.accepted().build()
    }
}
