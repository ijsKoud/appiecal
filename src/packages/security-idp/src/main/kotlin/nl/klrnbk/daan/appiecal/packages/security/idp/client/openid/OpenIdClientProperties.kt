package nl.klrnbk.daan.appiecal.packages.security.idp.client.openid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "appiecal.spring.security.authorization")
@EnableConfigurationProperties(OpenIdClientJwksProperties::class)
data class OpenIdClientProperties(
    val jwks: OpenIdClientJwksProperties,

    @Value("\${appiecal.spring.security.authorization.m2m:null}")
    val m2m: M2MProperties? = null,
)

@ConfigurationProperties(prefix = "appiecal.spring.security.authorization.jwks")
data class OpenIdClientJwksProperties(
    val uri: String,
    val refreshInterval: String,
)

data class M2MProperties(
    val url: String,
    val username: String,
    val password: String,
    val clientId: String,
    val scope: String,
)
