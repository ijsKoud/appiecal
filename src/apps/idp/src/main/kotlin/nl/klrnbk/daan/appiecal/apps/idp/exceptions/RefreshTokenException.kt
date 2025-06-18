package nl.klrnbk.daan.appiecal.apps.idp.exceptions

import nl.klrnbk.daan.appiecal.packages.common.exceptions.ApiException
import org.springframework.http.HttpStatus

class RefreshTokenException(
    detail: String,
    instance: String? = null,
) : ApiException(HttpStatus.CONFLICT, "Unable to refresh access token", detail, instance)
