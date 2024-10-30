package dev.ijskoud.appiecal.ah.rooster

import java.util.*

class RoosterService {
    private val repository: Repository = Repository()

    companion object {
        private var instance: RoosterService? = null

        fun getInstance(): RoosterService {
            if (instance == null) {
                instance = RoosterService()
            }

            return instance!!
        }
    }

    /**
     * Fetches the rooster
     * @param startDate The date the data should begin
     */
    suspend fun getRooster(startDate: Date? = null): List<Event> {
        val rooster = repository.getRooster(startDate) ?: return emptyList()
        val events = rooster.data.scheduleByWeek.map { schedule ->
            val start = Date.from(convertToInstant(schedule.startTime))
            val endDate = Date.from(convertToInstant(schedule.endTime))
            val storeName = schedule.store.abbreviatedDisplayName.replace(schedule.store.location, "")
            val teamNames = schedule.teamNames.map { teamName ->
                Teams.entries.find { team -> team.team == teamName }!!
            }

            Event(
                startDate = start,
                endDate = endDate,
                minutes = schedule.minutes,
                paidMinutes = schedule.paidMinutes,
                sickMinutes = schedule.sickMinutes,
                leaveMinutes = schedule.leaveMinutes,
                teamNames = teamNames,
                storeName = storeName.trim()
            )
        }

        return events
    }
}