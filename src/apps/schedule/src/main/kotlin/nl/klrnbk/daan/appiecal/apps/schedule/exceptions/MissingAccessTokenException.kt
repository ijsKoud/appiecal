package nl.klrnbk.daan.appiecal.apps.schedule.exceptions

import nl.klrnbk.daan.appiecal.packages.common.exceptions.ApiException
import org.springframework.http.HttpStatus

class MissingAccessTokenException(
    detail: String,
) : ApiException(
        HttpStatus.CONFLICT,
        "Unable to sync the schedule due to invalid or missing access token (Azure Entra Link setup correctly?)",
        detail,
    )
