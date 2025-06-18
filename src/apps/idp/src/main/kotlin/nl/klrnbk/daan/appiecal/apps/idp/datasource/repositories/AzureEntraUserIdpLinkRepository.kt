package nl.klrnbk.daan.appiecal.apps.idp.datasource.repositories

import nl.klrnbk.daan.appiecal.apps.idp.datasource.models.AzureEntraUserIdpLinkModel
import org.springframework.data.jpa.repository.JpaRepository

interface AzureEntraUserIdpLinkRepository : JpaRepository<AzureEntraUserIdpLinkModel, String>
