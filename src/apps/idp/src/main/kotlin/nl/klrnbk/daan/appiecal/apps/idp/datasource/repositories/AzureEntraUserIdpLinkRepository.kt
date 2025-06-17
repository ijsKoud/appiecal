package nl.klrnbk.daan.appiecal.apps.idp.datasource.repositories

import nl.klrnbk.daan.appiecal.apps.idp.datasource.models.AzureEntraUserIdpLink
import org.springframework.data.jpa.repository.JpaRepository

interface AzureEntraUserIdpLinkRepository : JpaRepository<AzureEntraUserIdpLink, String>
