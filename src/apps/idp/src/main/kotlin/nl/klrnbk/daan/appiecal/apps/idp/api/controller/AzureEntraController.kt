package nl.klrnbk.daan.appiecal.apps.idp.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import nl.klrnbk.daan.appiecal.apps.idp.api.facade.AzureEntraFacade
import nl.klrnbk.daan.appiecal.packages.common.responses.error.BaseErrorResponses
import nl.klrnbk.daan.appiecal.packages.security.idp.models.JwtAuthenticationToken
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/entra")
@BaseErrorResponses
class AzureEntraController(
    private val azureEntraFacade: AzureEntraFacade,
) {
    @GetMapping("/start", produces = ["text/plain"])
    @Operation(summary = "Start the Azure Entra authentication flow (returns login-page url)")
    @ApiResponse(
        responseCode = "200",
        description = "Get the azure entra authentication url",
        content = [
            Content(
                mediaType = "text/plain",
                schema = Schema(implementation = String::class),
                examples = [ExampleObject(value = "https://example.com/")],
            ),
        ],
    )
    fun getAzureEntraUrl(): String = azureEntraFacade.getAzureEntraUrl()

    @PostMapping("/end")
    @SecurityRequirement(name = "api-key")
    @Operation(
        summary = "Link IDP user to Azure Entra account credentials (access_token, refresh_token)",
        description = "Note: Existing links will be overwritten!",
    )
    @ApiResponse(
        responseCode = "204",
        description = "Link between Azure Entra and Idp has been made",
        content = [
            Content(
                mediaType = "*/*",
                schema = Schema(implementation = Void::class),
            ),
        ],
    )
    @PreAuthorize("@scopes.hasScope(authentication, 'https://klrnbk.nl/projects/appiecal:use')")
    fun linkUserWithAzureEntra(
        @RequestParam("code") authorizationCode: String,
        authentication: JwtAuthenticationToken,
    ): ResponseEntity<Unit> {
        azureEntraFacade.linkUserWithAzureEntra(authorizationCode, authentication.principal)
        return ResponseEntity.noContent().build()
    }
}
