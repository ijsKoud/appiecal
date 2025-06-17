package nl.klrnbk.daan.appiecal.packages.common.exceptions

import org.springframework.http.HttpStatus

class JpaException(
    detail: String,
    instance: String? = null,
) : ApiException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Error while performing database operation",
        detail,
        instance,
    )
