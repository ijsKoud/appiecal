package nl.klrnbk.daan.appiecal.apps.sync.api.facade

import nl.klrnbk.daan.appiecal.apps.sync.api.models.store.StoreInformation
import nl.klrnbk.daan.appiecal.apps.sync.api.service.StoreService
import org.springframework.stereotype.Service

@Service
class StoreFacade(
    private val storeService: StoreService,
) {
    fun getStoreInformation(storeId: String): StoreInformation = storeService.getStoreInformation(storeId)
}
