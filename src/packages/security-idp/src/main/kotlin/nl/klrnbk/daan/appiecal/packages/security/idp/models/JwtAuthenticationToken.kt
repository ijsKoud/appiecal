package nl.klrnbk.daan.appiecal.packages.security.idp.models

import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import nl.klrnbk.daan.appiecal.packages.security.idp.constants.AUTHORITY_GROUP_PREFIX
import nl.klrnbk.daan.appiecal.packages.security.idp.constants.AUTHORITY_SCOPE_PREFIX
import nl.klrnbk.daan.appiecal.packages.security.idp.constants.SERVICE_ACCOUNT_SCOPE
import nl.klrnbk.daan.appiecal.packages.security.idp.service.OpenIdService
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class JwtAuthenticationToken(
    val decodedJWT: DecodedJWT,
    val openIdService: OpenIdService,
) : AbstractAuthenticationToken(getAuthoritiesList(decodedJWT.claims)) {
    val scopes: List<String> = getScopes(decodedJWT.claims)
    val groups: List<String> = getGroups(decodedJWT.claims)

    override fun getCredentials(): String = decodedJWT.token

    override fun getPrincipal(): String = decodedJWT.subject

    override fun isAuthenticated(): Boolean = openIdService.verifyJwt(credentials)

    fun isServiceAccount(): Boolean = scopes.contains(SERVICE_ACCOUNT_SCOPE)

    companion object {
        private fun getAuthoritiesList(claims: Map<String, Claim>): MutableList<GrantedAuthority?> {
            val authorities: MutableList<GrantedAuthority?> = ArrayList()

            val groups = getGroups(claims)
            groups.forEach { authorities.add(SimpleGrantedAuthority("$AUTHORITY_GROUP_PREFIX$it")) }

            val scopes = getScopes(claims)
            scopes.forEach { authorities.add(SimpleGrantedAuthority("$AUTHORITY_SCOPE_PREFIX$it")) }

            return authorities
        }

        private fun getScopes(claims: Map<String, Claim>): List<String> {
            val scopeClaims = claims.getOrElse("scope") { null }
            val scopes = scopeClaims?.asString().orEmpty().split(" ")

            return scopes
        }

        private fun getGroups(claims: Map<String, Claim>): List<String> {
            val groupsClaims = claims.getOrElse("groups") { null }
            return groupsClaims?.asList(String::class.java).orEmpty()
        }
    }
}
