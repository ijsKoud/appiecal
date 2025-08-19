package nl.klrnbk.daan.appiecal.apps.sync.clients.store.models

data class StoreInformationResponse(
    val data: StoreInformationResponseObject,
)

data class StoreInformationResponseObject(
    val storesInformation: StoreInformationResponseObjectData,
)

data class StoreInformationResponseObjectData(
    val id: Int,
    val name: String,
    val address: StoreInformationRequestBodyAddress,

)

data class StoreInformationRequestBodyAddress(
    val street: String,
    val city: String,
    val postalCode: String,
    val countryCode: String,
    val houseNumber: String,
    val houseNumberExtra: String?,
)
