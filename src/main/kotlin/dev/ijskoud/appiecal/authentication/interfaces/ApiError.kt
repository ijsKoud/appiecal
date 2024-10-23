package dev.ijskoud.appiecal.authentication.interfaces

/**
 * Raw ApiError class
 */
data class ApiError(
    val error: String,
    val error_description: String,
    val error_codes: List<Int>,
    val timestamp: String,
    val trace_id: String,
    val correlation_id: String
)