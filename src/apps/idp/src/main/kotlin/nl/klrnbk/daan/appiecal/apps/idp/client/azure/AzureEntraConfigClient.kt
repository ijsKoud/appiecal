package nl.klrnbk.daan.appiecal.apps.idp.client.azure

import nl.klrnbk.daan.appiecal.apps.idp.config.AzureEntraConfig
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component

@Component
@EnableConfigurationProperties(AzureEntraConfig::class)
class AzureEntraConfigClient(
    val config: AzureEntraConfig,
)
