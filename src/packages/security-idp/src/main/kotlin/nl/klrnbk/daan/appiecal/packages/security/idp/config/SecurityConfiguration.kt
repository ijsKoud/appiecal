package nl.klrnbk.daan.appiecal.packages.security.idp.config

import nl.klrnbk.daan.appiecal.packages.security.idp.config.customizer.ExpressionCustomizer
import nl.klrnbk.daan.appiecal.packages.security.idp.constants.DEFAULT_UNAUTHENTICATED_ROUTES
import nl.klrnbk.daan.appiecal.packages.security.idp.filters.JwtAuthenticationFilter
import nl.klrnbk.daan.appiecal.packages.security.idp.filters.ScopesExpressionHandler
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@AutoConfiguration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(SecurityConfigurationProperties::class)
class SecurityConfiguration(
    val properties: SecurityConfigurationProperties,
    val filter: JwtAuthenticationFilter,
) {
    @Bean
    fun scopes() = ScopesExpressionHandler()

    @Bean
    fun openApiOperationCustomizer(): OperationCustomizer = ExpressionCustomizer()

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val unauthenticatedRoutes = DEFAULT_UNAUTHENTICATED_ROUTES + properties.unauthenticatedRoutes

        http {
            authorizeHttpRequests {
                unauthenticatedRoutes.forEach { authorize(it, permitAll) }
                authorize(anyRequest, authenticated)
            }
            exceptionHandling {
                authenticationEntryPoint = CustomAuthenticationEntryPoint()
            }
            csrf {
                disable()
            }
            anonymous {
                disable()
            }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            headers {
                cacheControl { }
            }
            addFilterBefore<BasicAuthenticationFilter>(filter)
        }

        return http.build()
    }
}
