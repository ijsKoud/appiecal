package nl.klrnbk.daan.appiecal.packages.spring.swagger.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(SwaggerConfigurationProperties::class)
class SwaggerConfiguration(
    private val properties: SwaggerConfigurationProperties,
) {
    @Bean
    fun customOpenAPI(): OpenAPI =
        OpenAPI()
            .info(
                getApiInfo(),
            ).components(
                getComponents(),
            )

    private fun getApiInfo(): Info =
        Info()
            .title(properties.title)
            .description(properties.description)

    private fun getComponents(): Components =
        Components().let {
            properties.headerApiKeys.forEach { headerApiKey ->
                it.addSecuritySchemes(
                    headerApiKey.keyId,
                    SecurityScheme()
                        .description(headerApiKey.description)
                        .name(headerApiKey.name)
                        .`in`(SecurityScheme.In.HEADER)
                        .type(SecurityScheme.Type.APIKEY),
                )
            }

            it
        }
}
