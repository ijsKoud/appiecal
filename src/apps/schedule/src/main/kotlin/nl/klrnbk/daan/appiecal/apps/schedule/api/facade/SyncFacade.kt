package nl.klrnbk.daan.appiecal.apps.schedule.api.facade

import nl.klrnbk.daan.appiecal.apps.schedule.api.service.ActivityService
import nl.klrnbk.daan.appiecal.apps.schedule.api.service.GqlService
import nl.klrnbk.daan.appiecal.apps.schedule.api.service.IdpService
import nl.klrnbk.daan.appiecal.apps.schedule.api.service.ShiftService
import nl.klrnbk.daan.appiecal.apps.schedule.api.service.SyncService
import nl.klrnbk.daan.appiecal.apps.schedule.constants.DATE_TIME_FORMATTER
import nl.klrnbk.daan.appiecal.apps.schedule.datasource.models.ShiftModel
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.syncing.SyncStatusResponse
import nl.klrnbk.daan.appiecal.packages.security.idp.models.JwtAuthenticationToken
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class SyncFacade(
    private val shiftService: ShiftService,
    private val activityService: ActivityService,
    private val syncService: SyncService,
    private val idpService: IdpService,
    private val gqlService: GqlService,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun syncSchedule(
        authentication: JwtAuthenticationToken,
        userId: String,
        startDate: ZonedDateTime,
        endDate: ZonedDateTime,
    ): SyncStatusResponse {
        val startDateString = startDate.format(DATE_TIME_FORMATTER)
        val endDateString = endDate.format(DATE_TIME_FORMATTER)

        val token = idpService.getAccessToken(authentication.credentials, userId)
        val shifts = gqlService.getFetchedShiftsWithActivities(token, startDateString, endDateString)

        val mappedShifts = shifts.map { ShiftModel.fromApiResponse(it, userId) }
        val existingShifts = shiftService.getShiftsBetweenDateRange(userId, startDate, endDate)

        val results = syncService.splitShiftsToCrud(existingShifts, mappedShifts)
        logger.info("Syncing shifts for user=$userId; ${results.shifts}")
        logger.info("Syncing activities for user=$userId; ${results.activities}")

        shiftService.deleteShifts(results.shifts.delete)
        activityService.deleteActivities(results.activities.delete)
        shiftService.saveOrUpdateShifts(results.shifts.new + results.shifts.update)

        return syncService.resultsToResponse(results)
    }
}
