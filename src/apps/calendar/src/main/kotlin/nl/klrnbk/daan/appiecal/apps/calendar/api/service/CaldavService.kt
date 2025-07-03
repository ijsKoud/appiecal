package nl.klrnbk.daan.appiecal.apps.calendar.api.service

import nl.klrnbk.daan.appiecal.apps.calendar.clients.caldav.CaldavClient
import org.springframework.stereotype.Service

@Service
class CaldavService(
    private val caldavClient: CaldavClient,
) {
    fun getPrincipal(
        baseUrl: String,
        authScope: String,
        authToken: String,
    ) = caldavClient.getPrincipal(baseUrl, authScope, authToken)

    fun getCalendarHomeSet(
        url: String,
        authScope: String,
        authToken: String,
    ) = caldavClient.getCalendarHomeSet(url, authScope, authToken)
}
