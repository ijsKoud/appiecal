package nl.klrnbk.daan.appiecal.packages.security.idp.filters

import nl.klrnbk.daan.appiecal.packages.security.idp.models.JwtAuthenticationToken

class ScopesExpressionHandler {
    fun hasScope(
        authentication: JwtAuthenticationToken?,
        scope: String,
    ): Boolean {
        if (authentication == null) return false
        return authentication.scopes.contains(scope)
    }

    fun hasAnyScope(
        authentication: JwtAuthenticationToken?,
        vararg scopes: String,
    ): Boolean {
        if (authentication == null) return false
        val scopesToCheck = listOf(*scopes)

        return scopesToCheck.any(authentication.scopes::contains)
    }
}
