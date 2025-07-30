package nl.klrnbk.daan.appiecal.apps.schedule.api.facade

import nl.klrnbk.daan.appiecal.apps.schedule.api.models.schedule.ScheduleShift
import nl.klrnbk.daan.appiecal.apps.schedule.api.service.ShiftService
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class ScheduleFacade(
    private val shiftService: ShiftService,
) {
    fun getScheduleFromId(
        userId: String,
        startDate: ZonedDateTime,
        endDate: ZonedDateTime,
    ): ScheduleResponse {
        val shifts = shiftService.getShiftsBetweenDateRange(userId, startDate, endDate)
        val responseShifts = shifts.map(ScheduleShift::fromModel)

        return ScheduleResponse(responseShifts)
    }
}
