package nl.klrnbk.daan.appiecal.packages.spring

import io.swagger.v3.oas.annotations.Hidden
import nl.klrnbk.daan.appiecal.packages.common.exceptions.ApiException
import nl.klrnbk.daan.appiecal.packages.common.responses.error.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@Hidden
@ControllerAdvice
class GlobalControllerAdvice {
    private val logger = LoggerFactory.getLogger(GlobalControllerAdvice::class.java)

    @ExceptionHandler(ApiException::class)
    fun handleApiExceptions(ex: ApiException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(ex.status.value())
            .header(HttpHeaders.CONTENT_TYPE, "application/problem+json")
            .body(
                ErrorResponse(
                    status = ex.status.value(),
                    type = ex.status.name,
                    message = ex.message!!,
                    detail = ex.detail,
                    instance = ex.instance,
                ),
            )

    @ExceptionHandler(Exception::class)
    fun handleExceptions(ex: Exception): ResponseEntity<ErrorResponse> {
        val httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        logger.error(ex.stackTraceToString())

        return ResponseEntity
            .status(httpStatus)
            .header(HttpHeaders.CONTENT_TYPE, "application/problem+json")
            .body(
                ErrorResponse(
                    status = httpStatus.value(),
                    type = httpStatus.name,
                    message = "An unexpected error occurred.",
                    detail = ex.message ?: "An unexpected error occurred.",
                    instance = ApiException.getExceptionInstance(),
                ),
            )
    }
}
