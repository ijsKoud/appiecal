package nl.klrnbk.daan.appiecal.apps.calendar.exceptions

import nl.klrnbk.daan.appiecal.packages.common.exceptions.ApiException
import org.springframework.http.HttpStatus

class MissingDavCredentialsException :
    ApiException(
        HttpStatus.UNAUTHORIZED,
        "CalDAV credentials have not been linked yet.",
        "Missing calDAV credentials, probably not linked to user yet",
    )
