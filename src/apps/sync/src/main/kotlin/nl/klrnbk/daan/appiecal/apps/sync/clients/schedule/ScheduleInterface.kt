package nl.klrnbk.daan.appiecal.apps.sync.clients.schedule

import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponse
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponseShift
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponseShiftActivity
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.syncing.SyncStatusResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleInterface {
    @POST("/v1/sync/{userId}")
    fun syncSchedule(
        @Header("x-authorization") authorization: String,
        @Path("userId") userId: String,
        @Query("start-date") startDate: String,
        @Query("end-date") endDate: String,
    ): Call<SyncStatusResponse>

    @GET("/v1/schedule/{userId}")
    fun getShifts(
        @Header("x-authorization") authorization: String,
        @Path("userId") userId: String,
        @Query("start-date") startDate: String,
        @Query("end-date") endDate: String,
    ): Call<ScheduleResponse<ScheduleResponseShift<ScheduleResponseShiftActivity>>>
}
