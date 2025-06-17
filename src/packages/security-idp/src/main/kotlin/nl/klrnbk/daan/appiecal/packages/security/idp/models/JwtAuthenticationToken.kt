package nl.klrnbk.daan.appiecal.packages.security.idp.models

import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import nl.klrnbk.daan.appiecal.packages.security.idp.service.OpenIdService
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class JwtAuthenticationToken(
    val decodedJWT: DecodedJWT,
    val openIdService: OpenIdService,
) : AbstractAuthenticationToken(getAuthoritiesList(decodedJWT.claims)) {
    override fun getCredentials(): String = decodedJWT.token

    override fun getPrincipal(): String = decodedJWT.subject

    override fun isAuthenticated(): Boolean = openIdService.verifyJwt(credentials)

    companion object {
        private fun getAuthoritiesList(claims: Map<String, Claim>): MutableList<GrantedAuthority?> {
            val authorities: MutableList<GrantedAuthority?> = ArrayList()

            val groupsClaims = claims.getOrElse("groups") { null }
            if (groupsClaims !== null && !groupsClaims.isNull) {
                val groups = groupsClaims.asList(String::class.java)
                groups.forEach { authorities.add(SimpleGrantedAuthority(it)) }
            }

            return authorities
        }
    }
}
