package nl.klrnbk.daan.appiecal.packages.security.idp.client.openid

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties

@ConfigurationProperties(prefix = "appiecal.spring.security.authorization")
@EnableConfigurationProperties(OpenIdClientJwksProperties::class)
data class OpenIdClientProperties(
    val jwks: OpenIdClientJwksProperties,
)

@ConfigurationProperties(prefix = "appiecal.spring.security.authorization.jwks")
data class OpenIdClientJwksProperties(
    val uri: String,
    val refreshInterval: String,
)
