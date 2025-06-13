package nl.klrnbk.daan.appiecal.packages.spring

import io.swagger.v3.oas.annotations.Hidden
import nl.klrnbk.daan.appiecal.packages.exceptions.models.ApiException
import nl.klrnbk.daan.appiecal.packages.exceptions.responses.error.ErrorResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@Hidden
@ControllerAdvice
class GlobalControllerAdvice {
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
}
