package nl.klrnbk.daan.appiecal.apps.sync.clients.calendar

import nl.klrnbk.daan.appiecal.packages.common.shared.services.calendar.models.CreateEventRequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CalendarInterface {
    @DELETE("/v1/events/{userId}/delete")
    fun deleteEvent(
        @Header("x-authorization") authorization: String,
        @Path("userId") userId: String,
        @Query("event-id") eventId: String,
    ): Call<Void>

    @POST("/v1/events/{userId}")
    @Headers("Content-Type: application/json")
    fun createEvent(
        @Header("x-authorization") authorization: String,
        @Path("userId") userId: String,
        @Body body: CreateEventRequestBody,
    ): Call<String>
}
