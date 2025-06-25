package nl.klrnbk.daan.appiecal.apps.schedule.datasource.repositories

import nl.klrnbk.daan.appiecal.apps.schedule.datasource.models.ShiftModel
import org.springframework.data.jpa.repository.JpaRepository
import java.time.ZonedDateTime

interface ShiftRepository : JpaRepository<ShiftModel, String> {
    fun findAllByUserIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(
        userId: String,
        fromDate: ZonedDateTime,
        toDate: ZonedDateTime,
    ): List<ShiftModel>
}
