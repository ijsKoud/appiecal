package nl.klrnbk.daan.appiecal.packages.common.exceptions

import org.springframework.http.HttpStatus

class MalformedResponseBodyException :
    ApiException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Downstream service returned malformed response body",
        "Empty or invalid response body returned",
    )
