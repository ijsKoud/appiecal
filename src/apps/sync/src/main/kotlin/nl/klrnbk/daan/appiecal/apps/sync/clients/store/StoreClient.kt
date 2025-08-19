package nl.klrnbk.daan.appiecal.apps.sync.clients.store

import nl.klrnbk.daan.appiecal.apps.sync.clients.store.models.StoreInformationRequestBody
import nl.klrnbk.daan.appiecal.apps.sync.clients.store.models.StoreInformationResponseObjectData
import nl.klrnbk.daan.appiecal.packages.common.retrofit.RetrofitClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class StoreClient(
    @Value("\${appiecal.sync.service-urls.store}") baseUrl: String,
) : RetrofitClient() {
    private val apiClient = getRetrofitClient(StoreApiInterface::class.java, baseUrl)

    fun getStoreInformation(storeId: Int): StoreInformationResponseObjectData {
        val call = apiClient.getStoreInformation(StoreInformationRequestBody(storeId))
        return handleApiCall(call).data.storesInformation
    }
}
