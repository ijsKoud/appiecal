package nl.klrnbk.daan.appiecal.packages.exceptions.responses.error

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponse(
    responseCode = "400",
    content = [
        Content(
            mediaType = "application/problem+json",
            schema = Schema(implementation = ErrorResponse::class),
            examples = [ExampleObject(value = ErrorResponse.BAD_REQUEST)],
        ),
    ],
)
annotation class BadRequestResponse

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponse(
    responseCode = "401",
    content = [
        Content(
            mediaType = "application/problem+json",
            schema = Schema(implementation = ErrorResponse::class),
            examples = [ExampleObject(value = ErrorResponse.UNAUTHORIZED)],
        ),
    ],
)
annotation class UnauthorizedResponse

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponse(
    responseCode = "404",
    content = [
        Content(
            mediaType = "application/problem+json",
            schema = Schema(implementation = ErrorResponse::class),
            examples = [ExampleObject(value = ErrorResponse.NOT_FOUND)],
        ),
    ],
)
annotation class NotFoundResponse

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponse(
    responseCode = "500",
    content = [
        Content(
            mediaType = "application/problem+json",
            schema = Schema(implementation = ErrorResponse::class),
            examples = [ExampleObject(value = ErrorResponse.INTERNAL_SERVER_ERROR)],
        ),
    ],
)
annotation class InternalServerErrorResponse

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponse(
    responseCode = "502",
    content = [
        Content(
            mediaType = "application/problem+json",
            schema = Schema(implementation = ErrorResponse::class),
            examples = [ExampleObject(value = ErrorResponse.BAD_GATEWAY)],
        ),
    ],
)
annotation class BadGatewayResponse

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponse(
    responseCode = "405",
    content = [
        Content(
            mediaType = "application/problem+json",
            schema = Schema(implementation = ErrorResponse::class),
            examples = [ExampleObject(value = ErrorResponse.METHOD_NOT_ALLOWED)],
        ),
    ],
)
annotation class MethodNotAllowedResponse

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponse(
    responseCode = "406",
    content = [
        Content(
            mediaType = "application/problem+json",
            schema = Schema(implementation = ErrorResponse::class),
            examples = [ExampleObject(value = ErrorResponse.NOT_ACCEPTABLE)],
        ),
    ],
)
annotation class NotAcceptableResponse

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponse(
    responseCode = "409",
    content = [
        Content(
            mediaType = "application/problem+json",
            schema = Schema(implementation = ErrorResponse::class),
            examples = [ExampleObject(value = ErrorResponse.CONFLICT)],
        ),
    ],
)
annotation class ConflictResponse

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponse(
    responseCode = "415",
    content = [
        Content(
            mediaType = "application/problem+json",
            schema = Schema(implementation = ErrorResponse::class),
            examples = [ExampleObject(value = ErrorResponse.UNSUPPORTED_MEDIA_TYPE)],
        ),
    ],
)
annotation class UnsupportedMediaTypeResponse

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponse(
    responseCode = "429",
    content = [
        Content(
            mediaType = "application/problem+json",
            schema = Schema(implementation = ErrorResponse::class),
            examples = [ExampleObject(value = ErrorResponse.RATE_LIMIT)],
        ),
    ],
)
annotation class RateLimitResponse

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponse(
    responseCode = "500",
    content = [
        Content(
            mediaType = "application/problem+json",
            schema = Schema(implementation = ErrorResponse::class),
            examples = [ExampleObject(value = ErrorResponse.SERVER_ERROR)],
        ),
    ],
)
annotation class ServerErrorResponse

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponse(
    responseCode = "503",
    content = [
        Content(
            mediaType = "application/problem+json",
            schema = Schema(implementation = ErrorResponse::class),
            examples = [ExampleObject(value = ErrorResponse.ENGINE_OVERLOADED)],
        ),
    ],
)
annotation class EngineOverloadedResponse

@BadRequestResponse
@UnauthorizedResponse
@NotFoundResponse
@InternalServerErrorResponse
@BadGatewayResponse
@MethodNotAllowedResponse
@NotAcceptableResponse
@ConflictResponse
@UnsupportedMediaTypeResponse
@RateLimitResponse
@ServerErrorResponse
@EngineOverloadedResponse
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseErrorResponses
