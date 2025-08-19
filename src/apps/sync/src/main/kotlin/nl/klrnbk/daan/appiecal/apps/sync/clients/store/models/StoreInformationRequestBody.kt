package nl.klrnbk.daan.appiecal.apps.sync.clients.store.models

class StoreInformationRequestBody(
    storeId: Int,
) {
    val operationName: String = "StoresInformation"
    val variables = mapOf("id" to storeId)
    val query: String =
        """
        query StoresInformation(${"$"}id: Int!) {
          storesInformation(id: ${"$"}id) {
              id
              name
              address {
                city
                countryCode
                houseNumber
                houseNumberExtra
                postalCode
                street
              }
              storeType
          }
        }
        """.trimIndent()
}
