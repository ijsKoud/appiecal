package nl.klrnbk.daan.appiecal.packages.security.idp.config

import com.auth0.jwt.exceptions.AlgorithmMismatchException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import nl.klrnbk.daan.appiecal.packages.exceptions.models.ApiException
import nl.klrnbk.daan.appiecal.packages.exceptions.responses.error.ErrorResponse
import nl.klrnbk.daan.appiecal.packages.security.idp.exceptions.InvalidJwtException
import nl.klrnbk.daan.appiecal.packages.security.idp.exceptions.JwtVerifyException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import java.io.Serializable

@Component
class CustomAuthenticationEntryPoint :
    AuthenticationEntryPoint,
    Serializable {
    private val log: Logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint::class.java)
    private val objectMapper = ObjectMapper()

    @Throws(IOException::class)
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        if (authException !is AuthenticationServiceException) {
            return handleErrorResponse(
                request,
                response,
                authException,
                HttpStatus.FORBIDDEN,
            )
        }

        val rootCause: Throwable = authException.cause!!
        val status =
            when (rootCause) {
                is JwtVerifyException -> HttpStatus.UNAUTHORIZED
                is InvalidJwtException -> HttpStatus.FORBIDDEN
                is AlgorithmMismatchException -> HttpStatus.UNAUTHORIZED
                is TokenExpiredException -> HttpStatus.FORBIDDEN
                is SignatureVerificationException -> HttpStatus.FORBIDDEN
                else -> HttpStatus.INTERNAL_SERVER_ERROR
            }

        handleErrorResponse(request, response, rootCause, status)
    }

    @Throws(IOException::class)
    private fun handleErrorResponse(
        request: HttpServletRequest,
        response: HttpServletResponse,
        ex: Throwable,
        status: HttpStatus,
    ) {
        log.warn(
            "Error setting up security context - uri: ${request.requestURI}, status: ${status.value()}, message: ${ex.message}",
        )

        val error =
            when (ex) {
                is ApiException -> {
                    ErrorResponse(
                        status = status.value(),
                        type = status.name,
                        message = ex.message ?: "Error setting up security context",
                        detail = ex.detail,
                        instance = request.requestURI,
                    )
                }

                else ->
                    ErrorResponse(
                        status = status.value(),
                        type = status.name,
                        message = "Error setting up security context",
                        detail = ex.message ?: "Error setting up security context",
                        instance = request.requestURI,
                    )
            }

        response.status = status.value()
        response.contentType = "application/problem+json"
        response.writer.use { writer ->
            writer.write(objectMapper.writeValueAsString(error))
            writer.flush()
        }
    }
}
