package nl.klrnbk.daan.appiecal.packages.security.idp.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "appiecal.spring.security")
data class SecurityConfigurationProperties(
    val unauthenticatedRoutes: List<String>,
)
