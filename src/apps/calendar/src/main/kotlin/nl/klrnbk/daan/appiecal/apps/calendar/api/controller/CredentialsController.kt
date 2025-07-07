package nl.klrnbk.daan.appiecal.apps.calendar.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import nl.klrnbk.daan.appiecal.apps.calendar.api.facade.CredentialsFacade
import nl.klrnbk.daan.appiecal.packages.common.responses.error.BaseErrorResponses
import nl.klrnbk.daan.appiecal.packages.security.idp.models.JwtAuthenticationToken
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@SecurityRequirement(name = "api-key")
@RequestMapping("/v1/credentials")
@BaseErrorResponses
class CredentialsController(
    private val credentialsFacade: CredentialsFacade,
) {
    @PostMapping("/link")
    @Operation(
        summary = "Link IDP user to a calDAV server",
        description = "Note: Existing links will be overwritten!",
    )
    @ApiResponse(
        responseCode = "204",
        description = "Link between the calDAV and Idp has been made",
        content = [
            Content(
                mediaType = "*/*",
                schema = Schema(implementation = Void::class),
            ),
        ],
    )
    @PreAuthorize("@scopes.hasScope(authentication, 'https://klrnbk.nl/projects/appiecal:use')")
    fun linkCaldavToUser(
        authentication: JwtAuthenticationToken,
        @RequestParam("base-url", required = true) baseUrl: String,
        @RequestParam("auth-scope", required = true) authScope: String,
        @RequestParam("auth-token", required = true) authToken: String,
    ) = credentialsFacade.linkCaldavToUser(authentication.principal, baseUrl, authScope, authToken)

    @DeleteMapping("/unlink")
    @Operation(
        summary = "Unlink IDP user from a calDAV server credentials",
    )
    @ApiResponse(
        responseCode = "204",
        description = "Credentials are removed and user is unlinked",
        content = [
            Content(
                mediaType = "*/*",
                schema = Schema(implementation = Void::class),
            ),
        ],
    )
    @PreAuthorize("@scopes.hasScope(authentication, 'https://klrnbk.nl/projects/appiecal:use')")
    fun unlinkUser(authentication: JwtAuthenticationToken) = credentialsFacade.unlinkUser(authentication.principal)
}
