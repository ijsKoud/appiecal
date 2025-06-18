package nl.klrnbk.daan.appiecal.apps.idp.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import nl.klrnbk.daan.appiecal.apps.idp.api.facade.TokenFacade
import nl.klrnbk.daan.appiecal.packages.common.responses.error.BaseErrorResponses
import nl.klrnbk.daan.appiecal.packages.common.responses.error.ErrorResponse
import nl.klrnbk.daan.appiecal.packages.security.idp.models.JwtAuthenticationToken
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/token/")
@SecurityRequirement(name = "api-key")
@BaseErrorResponses
class TokenController(
    private val tokenFacade: TokenFacade,
) {
    @GetMapping("/access-token")
    @Operation(
        summary = "Retrieve an access token to interact with the @AH APIs",
    )
    @ApiResponse(
        responseCode = "200",
        description = "(Fresh) usable access token ready to use",
        content = [
            Content(
                mediaType = "text/plain",
                schema = Schema(implementation = String::class),
                examples = [ExampleObject(value = "xxx.yyy.zzz")],
            ),
        ],
    )
    @PreAuthorize("@scopes.hasScope(authentication, 'https://klrnbk.nl/projects/appiecal:use')")
    fun getAccessToken(authentication: JwtAuthenticationToken): String? = tokenFacade.getAccessToken(authentication.principal)

    @PostMapping("/force-refresh-token")
    @Operation(
        summary = "Retrieve a fresh access token to interact with the @AH APIs",
    )
    @ApiResponse(
        responseCode = "200",
        description = "Fresh access token ready to use",
        content = [
            Content(
                mediaType = "text/plain",
                schema = Schema(implementation = String::class),
                examples = [ExampleObject(value = "xxx.yyy.zzz")],
            ),
        ],
    )
    @ApiResponse(
        responseCode = "409",
        description = "Unable to refresh access token due to missing Azure Entra credentials (refresh_token)",
        content = [
            Content(
                mediaType = "application/problem+json",
                schema = Schema(implementation = ErrorResponse::class),
                examples = [
                    ExampleObject(
                        value = ErrorResponse.CONFLICT,
                    ),
                ],
            ),
        ],
    )
    @PreAuthorize("@scopes.hasScope(authentication, 'https://klrnbk.nl/projects/appiecal:use')")
    fun forceRefreshAccessToken(authentication: JwtAuthenticationToken): String? =
        tokenFacade.getAccessToken(authentication.principal, true)
}
