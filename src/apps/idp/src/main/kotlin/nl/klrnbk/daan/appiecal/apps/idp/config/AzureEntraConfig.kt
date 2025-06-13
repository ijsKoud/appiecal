package nl.klrnbk.daan.appiecal.apps.idp.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties

@ConfigurationProperties(prefix = "appiecal.azure.entra")
@EnableConfigurationProperties(AzureEntraCredentialsConfig::class)
data class AzureEntraConfig(
    val authenticationUrl: String,
    val azureEntraUrl: String,
    val credentials: AzureEntraCredentialsConfig,
)

@ConfigurationProperties(prefix = "appiecal.azure.entra.credentials")
data class AzureEntraCredentialsConfig(
    val clientId: String,
    val scopes: String,
)
