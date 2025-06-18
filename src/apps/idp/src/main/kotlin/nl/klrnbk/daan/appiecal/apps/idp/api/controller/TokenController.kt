package nl.klrnbk.daan.appiecal.apps.idp.api.controller

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import nl.klrnbk.daan.appiecal.apps.idp.api.facade.TokenFacade
import nl.klrnbk.daan.appiecal.packages.security.idp.models.JwtAuthenticationToken
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/token/")
@SecurityRequirement(name = "api-key")
class TokenController(
    private val tokenFacade: TokenFacade,
) {
    @GetMapping("/access-token")
    @PreAuthorize("@scopes.hasScope(authentication, 'https://klrnbk.nl/projects/appiecal:use')")
    fun getAccessToken(authentication: JwtAuthenticationToken): String? = tokenFacade.getAccessToken(authentication.principal)
}
