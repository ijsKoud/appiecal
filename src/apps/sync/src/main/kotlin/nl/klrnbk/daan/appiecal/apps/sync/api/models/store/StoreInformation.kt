package nl.klrnbk.daan.appiecal.apps.sync.api.models.store

import nl.klrnbk.daan.appiecal.apps.sync.clients.store.models.StoreInformationRequestBodyAddress

data class StoreInformation(
    val id: String,
    val name: String,
    val address: String,
) {
    companion object {
        fun addressFromObject(obj: StoreInformationRequestBodyAddress): String {
            val houseNumberComplete = "${obj.houseNumber}${obj.houseNumberExtra ?: ""}"
            val postalCodeWithCity = "${obj.postalCode} ${obj.city}"
            val country = countryCodeToCountry.getOrDefault(obj.countryCode, "")

            return "${obj.street} $houseNumberComplete, $postalCodeWithCity, $country"
        }

        val countryCodeToCountry = mapOf("NLD" to "Nederland")
    }
}
