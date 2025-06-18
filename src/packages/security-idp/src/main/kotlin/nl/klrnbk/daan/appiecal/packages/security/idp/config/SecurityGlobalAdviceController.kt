package nl.klrnbk.daan.appiecal.packages.security.idp.config

import io.swagger.v3.oas.annotations.Hidden
import nl.klrnbk.daan.appiecal.packages.common.exceptions.ApiException
import nl.klrnbk.daan.appiecal.packages.common.responses.error.ErrorResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@Hidden
@ControllerAdvice
class SecurityGlobalAdviceController {
    @ExceptionHandler(AuthorizationDeniedException::class)
    fun handleAuthorizationDeniedException(ex: AuthorizationDeniedException): ResponseEntity<ErrorResponse> {
        val httpStatus = HttpStatus.FORBIDDEN

        return ResponseEntity
            .status(httpStatus)
            .header(HttpHeaders.CONTENT_TYPE, "application/problem+json")
            .body(
                ErrorResponse(
                    status = httpStatus.value(),
                    type = httpStatus.name,
                    message = "You do not have permission to perform this operation",
                    detail = ex.message ?: "You do not have permission to perform this operation.",
                    instance = ApiException.getExceptionInstance(),
                ),
            )
    }
}
