package nl.klrnbk.daan.appiecal.apps.schedule.api.facade

import nl.klrnbk.daan.appiecal.apps.schedule.api.service.ActivityService
import nl.klrnbk.daan.appiecal.apps.schedule.api.service.GqlService
import nl.klrnbk.daan.appiecal.apps.schedule.api.service.IdpService
import nl.klrnbk.daan.appiecal.apps.schedule.api.service.ShiftService
import nl.klrnbk.daan.appiecal.apps.schedule.api.service.SyncService
import nl.klrnbk.daan.appiecal.apps.schedule.constants.DATE_TIME_FORMATTER
import nl.klrnbk.daan.appiecal.apps.schedule.datasource.models.ShiftModel
import nl.klrnbk.daan.appiecal.packages.security.idp.models.JwtAuthenticationToken
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

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
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ) {
        val startDateString = startDate.format(DATE_TIME_FORMATTER)
        val endDateString = endDate.format(DATE_TIME_FORMATTER)

        val token = idpService.getAccessToken(authentication.credentials, userId)
        val shifts = gqlService.getFetchedShiftsWithActivities(token, startDateString, endDateString)

        val mappedShifts = shifts.map(ShiftModel::fromApiResponse)
        val existingShifts = shiftService.getShiftsBetweenDateRange(startDate, endDate)

        val result = syncService.splitShiftsToCrud(existingShifts, mappedShifts)
        logger.info("Syncing shifts for user=$userId; ${result.shifts}")
        logger.info("Syncing activities for user=$userId; ${result.activities}")

        shiftService.deleteShifts(result.shifts.delete)
        shiftService.saveOrUpdateShifts(result.shifts.new + result.shifts.update)
        activityService.deleteActivities(result.activities.delete)
    }
}
