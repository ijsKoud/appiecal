package nl.klrnbk.daan.appiecal.apps.idp.api.controller

import nl.klrnbk.daan.appiecal.apps.idp.api.facade.AzureEntraFacade
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/entra")
class AzureEntraController(
    private val azureEntraFacade: AzureEntraFacade,
) {
    @GetMapping("/v1/authentication/start")
    fun getAzureEntraUrl(): String = azureEntraFacade.getAzureEntraUrl()
}
