package nl.klrnbk.daan.appiecal.apps.schedule.datasource.repositories

import nl.klrnbk.daan.appiecal.apps.schedule.datasource.models.ShiftModel
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ShiftRepository : JpaRepository<ShiftModel, String> {
    fun findAllByStartDateGreaterThanEqualAndEndDateLessThanEqual(
        fromDate: LocalDateTime,
        toDate: LocalDateTime,
    ): List<ShiftModel>
}
