package nl.klrnbk.daan.appiecal.apps.idp.api.controller

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import nl.klrnbk.daan.appiecal.apps.idp.api.facade.AzureEntraFacade
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/entra")
class AzureEntraController(
    private val azureEntraFacade: AzureEntraFacade,
) {
    @GetMapping("/start", produces = ["text/plain"])
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
    fun linkUserWithAzureEntra(
        @RequestParam("code") authorizationCode: String,
    ): String = azureEntraFacade.linkUserWithAzureEntra(authorizationCode)
}
