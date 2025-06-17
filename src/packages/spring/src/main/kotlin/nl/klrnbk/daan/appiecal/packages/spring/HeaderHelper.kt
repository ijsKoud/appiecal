package nl.klrnbk.daan.appiecal.packages.spring

import nl.klrnbk.daan.appiecal.packages.common.exceptions.ApiException
import org.springframework.http.HttpStatus
import org.springframework.util.StringUtils
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * Returns the headers starting with 'x-' as a map.
 *
 * For example: `'x-authorization': 'Bearer ...', 'x-content-type': 'application/json'`.
 */
fun getXPrefixHeaders(): Map<String, String> {
    val sra = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes)
    val request = sra.request

    val headerNames = request.headerNames
    val headersToCopy =
        headerNames
            .iterator()
            .asSequence()
            .mapNotNull { it.takeIf { StringUtils.startsWithIgnoreCase(it, "x-") } }

    val headers =
        headersToCopy
            .map {
                it to request.getHeader(it)
            }.toMap()

    return headers
}

/**
 * Returns the authorization header value if any.
 *
 * For example: `Bearer ...` or `null`.
 */
fun getAuthorization(): String? = getXPrefixHeaders()["x-authorization"]

/**
 * Returns the authorization header value, throws an error if nothing is available.
 */
fun getAuthorizationOrError(): String {
    val value = getAuthorization()
    if (value.isNullOrBlank()) {
        throw ApiException(
            HttpStatus.UNAUTHORIZED,
            "You have to be logged in to do this.",
            "No x-authorization header is present.",
        )
    }

    return value
}
