package nl.klrnbk.daan.appiecal.apps.calendar.api.facade

import nl.klrnbk.daan.appiecal.apps.calendar.api.models.CalendarListEntryResponse
import nl.klrnbk.daan.appiecal.apps.calendar.api.service.CaldavService
import nl.klrnbk.daan.appiecal.apps.calendar.api.service.CalendarCredentialsService
import nl.klrnbk.daan.appiecal.apps.calendar.exceptions.MissingDavCredentialsException
import org.springframework.stereotype.Service

@Service
class CalendarFacade(
    private val caldavService: CaldavService,
    private val calendarCredentialsService: CalendarCredentialsService,
) {
    fun getCalendarList(userId: String): List<CalendarListEntryResponse> {
        val credentials = calendarCredentialsService.getCredentials(userId)
        if (credentials == null) throw MissingDavCredentialsException()

        return caldavService.getCalendarList(credentials.calendarHomeSetUrl, credentials.scope, credentials.token)
    }
}
