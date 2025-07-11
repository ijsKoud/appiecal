package nl.klrnbk.daan.appiecal.apps.calendar.datasource.repositories

import nl.klrnbk.daan.appiecal.apps.calendar.datasource.models.CalendarCredentialsModel
import org.springframework.data.jpa.repository.JpaRepository

interface CalendarCredentialsRepository : JpaRepository<CalendarCredentialsModel, String> {
    fun findByUserId(userId: String): List<CalendarCredentialsModel>

    fun existsByUserId(userId: String): Boolean
}
