package dev.ijskoud.appiecal.rooster.interfaces

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Header

/**
 * Defines the possible API calls that retrofit can make
 */
interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/external/ah/rtp/dex/graphql/")
    suspend fun getRooster(
        @Header("Authorization") authorization: String,
        @Body request: RoosterRequest,
    ): Response<RoosterResponse>
}

/*
 * The API response structures
 */

data class RoosterRequest(
    val operationName: String,
    val variables: AlbertHeijnVariables,
    val query: String
)

data class AlbertHeijnVariables(
    val startDate: String,
    val endDate: String
)

data class RoosterResponse(
    val data: ScheduleByWeekData
)

data class ScheduleByWeekData(
    val scheduleByWeek: List<Schedule>
)

data class Schedule(
    val startTime: String,
    val endTime: String,
    val minutes: Int,
    val storeId: String,
    val leaveMinutes: Int,
    val store: Store,
    val paidMinutes: Int,
    val sickMinutes: Int,
    val teamNames: List<String>
)

data class Store(
    val abbreviatedDisplayName: String,
    val location: String,
    val id: String,
)