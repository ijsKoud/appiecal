package nl.klrnbk.daan.appiecal.apps.schedule.datasource.repositories

import nl.klrnbk.daan.appiecal.apps.schedule.datasource.models.ActivityModel
import org.springframework.data.jpa.repository.JpaRepository

interface ActivityRepository : JpaRepository<ActivityModel, String>
