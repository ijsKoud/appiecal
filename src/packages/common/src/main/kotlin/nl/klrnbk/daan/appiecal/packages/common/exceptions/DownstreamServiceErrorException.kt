package nl.klrnbk.daan.appiecal.packages.common.exceptions

import org.springframework.http.HttpStatus

class DownstreamServiceErrorException(
    status: Int,
    detail: String,
    instance: String? = null,
) : ApiException(
        HttpStatus.valueOf(status),
        "Downstream service responded with $status status",
        detail,
        instance,
    )
