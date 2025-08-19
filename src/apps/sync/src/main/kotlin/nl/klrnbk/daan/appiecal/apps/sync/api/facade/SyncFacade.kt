package nl.klrnbk.daan.appiecal.apps.sync.api.facade

import nl.klrnbk.daan.appiecal.apps.sync.api.service.CalendarService
import nl.klrnbk.daan.appiecal.apps.sync.api.service.ScheduleService
import nl.klrnbk.daan.appiecal.apps.sync.api.service.StoreService
import nl.klrnbk.daan.appiecal.apps.sync.helpers.getStoreIdsFromShifts
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.syncing.SyncStatusResponse
import nl.klrnbk.daan.appiecal.packages.security.idp.service.OpenIdService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class SyncFacade(
    private val scheduleService: ScheduleService,
    private val calendarService: CalendarService,
    private val openIdService: OpenIdService,
    private val storeService: StoreService,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun syncScheduleWithCalDav(
        userId: String,
        startDate: ZonedDateTime,
        endDate: ZonedDateTime,
    ): SyncStatusResponse {
        val token = openIdService.getM2MToken()
        val syncResults = scheduleService.syncSchedule(token.idToken, userId, startDate.toString(), endDate.toString())
        val syncedShifts =
            scheduleService.getScheduleFromSyncResults(
                token.idToken,
                userId,
                startDate.toString(),
                endDate.toString(),
                syncResults,

            )

        val storeIds = getStoreIdsFromShifts(syncedShifts.new + syncedShifts.updated)
        val storesInformation = storeIds.map(storeService::getStoreInformation)

        calendarService.deleteEvents(token.idToken, userId, syncedShifts.deleted)
        syncedShifts.new.forEach { calendarService.createOrUpdateEvent(token.idToken, userId, it, storesInformation) }
        syncedShifts.updated.forEach { calendarService.createOrUpdateEvent(token.idToken, userId, it, storesInformation) }

        logger.info("Successfully synced schedule for user $userId;$syncedShifts")
        return syncResults
    }
}
