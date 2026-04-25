package nl.klrnbk.daan.appiecal.apps.sync.api.datasource.repositories

import nl.klrnbk.daan.appiecal.apps.sync.api.datasource.models.AutomaticSyncModel
import org.springframework.data.jpa.repository.JpaRepository

interface AutomaticSyncRepository : JpaRepository<AutomaticSyncModel, String>
