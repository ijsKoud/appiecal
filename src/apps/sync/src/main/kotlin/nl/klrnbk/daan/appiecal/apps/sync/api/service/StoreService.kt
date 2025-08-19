package nl.klrnbk.daan.appiecal.apps.sync.api.service

import nl.klrnbk.daan.appiecal.apps.sync.api.models.store.StoreInformation
import nl.klrnbk.daan.appiecal.apps.sync.clients.store.StoreClient
import org.springframework.stereotype.Service

@Service
class StoreService(
    private val storeClient: StoreClient,
) {
    fun getStoreInformation(storeId: String): StoreInformation {
        val storeInformationFromClient = storeClient.getStoreInformation(storeId.toInt())
        val address = StoreInformation.addressFromObject(storeInformationFromClient.address)

        return StoreInformation(
            id = storeId,
            name = storeInformationFromClient.address.street,
            address = address,
        )
    }
}
