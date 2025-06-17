package nl.klrnbk.daan.appiecal.packages.security.idp.filters

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import nl.klrnbk.daan.appiecal.packages.security.idp.exceptions.InvalidJwtException
import nl.klrnbk.daan.appiecal.packages.security.idp.helpers.resolveJwtFromRequest
import nl.klrnbk.daan.appiecal.packages.security.idp.models.JwtAuthenticationToken
import nl.klrnbk.daan.appiecal.packages.security.idp.service.OpenIdService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    val openIdService: OpenIdService,
) : OncePerRequestFilter() {
    private val securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy()
    private val securityContextRepository = RequestAttributeSecurityContextRepository()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val token = resolveJwtFromRequest(request)
        if (token.isNullOrBlank()) return filterChain.doFilter(request, response)

        val decoded = openIdService.decodeAndVerifyJwt(token)
        if (decoded == null) throw InvalidJwtException()

        val context = securityContextHolderStrategy.createEmptyContext()
        context.authentication = JwtAuthenticationToken(decoded, openIdService)

        securityContextHolderStrategy.context = context
        securityContextRepository.saveContext(context, request, response)

        filterChain
            .doFilter(request, response)
    }
}
