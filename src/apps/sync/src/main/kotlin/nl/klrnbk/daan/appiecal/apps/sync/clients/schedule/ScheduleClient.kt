package nl.klrnbk.daan.appiecal.apps.sync.clients.schedule

import nl.klrnbk.daan.appiecal.packages.common.retrofit.RetrofitClient
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponse
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponseShift
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponseShiftActivity
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.syncing.SyncStatusResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ScheduleClient(
    @Value("\${appiecal.sync.service-urls.schedule}") baseUrl: String,
) : RetrofitClient() {
    private val apiClient = getRetrofitClient(ScheduleInterface::class.java, baseUrl)

    fun syncSchedule(
        token: String,
        userId: String,
        startDate: String,
        endDate: String,
    ): SyncStatusResponse {
        val call = apiClient.syncSchedule(token, userId, startDate, endDate)
        return handleApiCall(call)
    }

    fun getShifts(
        token: String,
        userId: String,
        startDate: String,
        endDate: String,
    ): ScheduleResponse<ScheduleResponseShift<ScheduleResponseShiftActivity>> {
        val call = apiClient.getShifts(token, userId, startDate, endDate)
        return handleApiCall(call)
    }
}
