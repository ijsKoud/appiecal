package nl.klrnbk.daan.appiecal.apps.schedule.exceptions

import nl.klrnbk.daan.appiecal.packages.common.exceptions.ApiException
import org.springframework.http.HttpStatus

class MissingAccessTokenException(
    detail: String,
) : ApiException(
        HttpStatus.CONFLICT,
        "Unable to sync the schedule due to invalid or missing access token (Azure Entra Link setup correctly?)",
        detail,
    ) {
    companion object {
        const val EXAMPLE =
            """
            {         
                "status": 409,
                "type": "CONFLICT",
                "message": "Unable to sync the schedule due to invalid or missing access token (Azure Entra Link setup correctly?)",
                "detail": "Expected token to be returned but got empty string instead",
                "instance": "/v1/sync/{userId}"
            }
            """
    }
}
