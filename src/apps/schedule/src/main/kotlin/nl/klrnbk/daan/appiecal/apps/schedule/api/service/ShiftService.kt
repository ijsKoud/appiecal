package nl.klrnbk.daan.appiecal.apps.schedule.api.service

import nl.klrnbk.daan.appiecal.apps.schedule.datasource.models.ShiftModel
import nl.klrnbk.daan.appiecal.apps.schedule.datasource.repositories.ShiftRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class ShiftService(
    private val shiftRepository: ShiftRepository,
) {
    fun getShiftsBetweenDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): List<ShiftModel> = shiftRepository.findAllByStartDateGreaterThanEqualAndEndDateLessThanEqual(startDate, endDate)

    fun saveOrUpdateShifts(shifts: List<ShiftModel>): List<ShiftModel> = shiftRepository.saveAll(shifts)

    fun deleteShifts(shifts: List<ShiftModel>) = shiftRepository.deleteAll(shifts)
}
