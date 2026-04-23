package nl.klrnbk.daan.appiecal.apps.sync.clients.store

import nl.klrnbk.daan.appiecal.apps.sync.clients.store.models.StoreInformationRequestBody
import nl.klrnbk.daan.appiecal.apps.sync.clients.store.models.StoreInformationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface StoreApiInterface {
    @POST("/gql")
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json",
        "origin: https://ah.nl",
        "Referer: https://ah.nl/winkels",
        "x-client-name: ah-store",
        "x-client-version: 1.16.16",
        "x-client-platform-type: web",
        "user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:147.0) Gecko/20100101 Firefox/147.0",
    )
    fun getStoreInformation(
        @Body body: StoreInformationRequestBody,
    ): Call<StoreInformationResponse>
}
