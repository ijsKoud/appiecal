package nl.klrnbk.daan.appiecal.apps.sync.api.service

import nl.klrnbk.daan.appiecal.apps.sync.api.datasource.models.AutomaticSyncModel
import nl.klrnbk.daan.appiecal.apps.sync.api.datasource.repositories.AutomaticSyncRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AutomaticSyncService(
    private val automaticSyncRepository: AutomaticSyncRepository,
) {
    fun setAutomaticSync(
        userId: String,
        state: Boolean,
    ) {
        val entity = AutomaticSyncModel(userId, state)
        automaticSyncRepository.save(entity)
    }

    fun getAutomaticSyncStatus(userId: String): Boolean {
        val entity = automaticSyncRepository.findByIdOrNull(userId)
        return entity?.active ?: false
    }

    fun getAllActiveSyncUsers(): List<String> {
        val entities = automaticSyncRepository.findAllByActive(true)
        return entities.map { it.userId }
    }
}
