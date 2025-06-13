package nl.klrnbk.daan.appiecal.packages.exceptions.models

import org.springframework.http.HttpStatus

class DownstreamServiceErrorException(
    status: HttpStatus,
    detail: String,
    instance: String? = null,
) : ApiException(status, "Downstream service responded with $status status", detail, instance)
