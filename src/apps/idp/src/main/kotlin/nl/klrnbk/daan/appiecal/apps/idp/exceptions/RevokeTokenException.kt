package nl.klrnbk.daan.appiecal.apps.idp.exceptions

import nl.klrnbk.daan.appiecal.packages.common.exceptions.ApiException
import org.springframework.http.HttpStatus

class RevokeTokenException(
    detail: String,
    instance: String? = null,
) : ApiException(HttpStatus.CONFLICT, "Unable to revoke the access and refresh token", detail, instance)
