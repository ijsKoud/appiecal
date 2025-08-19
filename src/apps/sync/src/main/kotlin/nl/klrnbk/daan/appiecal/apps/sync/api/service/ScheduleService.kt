package nl.klrnbk.daan.appiecal.apps.sync.api.service

import nl.klrnbk.daan.appiecal.apps.sync.api.models.schedule.SyncAndGetScheduleResponse
import nl.klrnbk.daan.appiecal.apps.sync.clients.schedule.ScheduleClient
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.syncing.SyncStatusResponse
import org.springframework.stereotype.Service

@Service
class ScheduleService(
    private val scheduleClient: ScheduleClient,
) {
    fun syncSchedule(
        authorization: String,
        userId: String,
        startDate: String,
        endDate: String,
    ): SyncStatusResponse = scheduleClient.syncSchedule(authorization, userId, startDate, endDate)

    fun getScheduleFromSyncResults(
        authorization: String,
        userId: String,
        startDate: String,
        endDate: String,
        syncResults: SyncStatusResponse,
    ): SyncAndGetScheduleResponse {
        val shifts = scheduleClient.getShifts(authorization, userId, startDate, endDate)

        val newShifts = shifts.shifts.filter { syncResults.shifts.new.contains(it.id.toString()) && !it.isFullAbsentDay() }
        val updatedShifts = shifts.shifts.filter { syncResults.shifts.update.contains(it.id.toString()) }

        return SyncAndGetScheduleResponse(newShifts, updatedShifts, syncResults.shifts.delete)
    }
}
