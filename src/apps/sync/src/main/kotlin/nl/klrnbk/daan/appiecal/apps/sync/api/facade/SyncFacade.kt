package nl.klrnbk.daan.appiecal.apps.sync.api.facade

import nl.klrnbk.daan.appiecal.apps.sync.api.service.AutomaticSyncService
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
    private val automaticSyncService: AutomaticSyncService,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun setAutomaticSync(
        userId: String,
        state: Boolean,
    ) = automaticSyncService.setAutomaticSync(userId, state)

    fun getAutomaticSyncStatus(userId: String): Boolean = automaticSyncService.getAutomaticSyncStatus(userId)

    fun forceCalendarSync(userId: String) {
        val dates = calendarService.getSyncDates()
        logger.info("Starting forced calendar sync at ${ZonedDateTime.now()};start=${dates.first},end=${dates.second}")

        val token = openIdService.getM2MToken()
        val shifts = scheduleService.fetchSyncedSchedule(token.idToken, userId, dates.first.toString(), dates.second.toString())

        val storeIds = getStoreIdsFromShifts(shifts.shifts)
        val storesInformation = storeIds.map(storeService::getStoreInformation)

        shifts.shifts.forEach { calendarService.createOrUpdateEvent(token.idToken, userId, it, storesInformation) }

        logger.info("Successfully force synced schedule for user $userId;${shifts.shifts.size} shifts")
    }

    fun periodicSync() {
        val users = automaticSyncService.getAllActiveSyncUsers()
        val dates = calendarService.getSyncDates()
        logger.info("Starting periodic sync at ${ZonedDateTime.now()};start=${dates.first},end=${dates.second}")

        for (user in users) {
            syncScheduleWithCalDav(
                user,
                dates.first,
                dates.second,
            )
        }

        logger.info("Ending periodic sync at ${ZonedDateTime.now()}")
    }

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
