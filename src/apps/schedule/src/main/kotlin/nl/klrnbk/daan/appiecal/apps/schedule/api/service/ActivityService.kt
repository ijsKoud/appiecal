package nl.klrnbk.daan.appiecal.apps.schedule.api.service

import nl.klrnbk.daan.appiecal.apps.schedule.datasource.models.ActivityModel
import nl.klrnbk.daan.appiecal.apps.schedule.datasource.repositories.ActivityRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ActivityService(
    private val activityRepository: ActivityRepository,
) {
    fun deleteActivities(activities: List<ActivityModel>) = activityRepository.deleteAll(activities)
}
