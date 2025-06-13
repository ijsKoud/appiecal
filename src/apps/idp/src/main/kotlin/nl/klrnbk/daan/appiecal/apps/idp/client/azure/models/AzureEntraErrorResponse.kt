package nl.klrnbk.daan.appiecal.apps.idp.client.azure.models

import com.google.gson.annotations.SerializedName

data class AzureEntraErrorResponse(
    @SerializedName("error_description") val description: String,
    @SerializedName("error_codes") val errorCodes: List<Int>,
    @SerializedName("trace_id") val traceId: String,
    @SerializedName("correlation_id") val correlationId: String,
    val error: String,
    val timestamp: String,
)
