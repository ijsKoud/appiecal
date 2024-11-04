package dev.ijskoud.appiecal.ah.rooster

import dev.ijskoud.appiecal.ah.rooster.interfaces.Schedule
import dev.ijskoud.appiecal.ah.rooster.shift.Shift
import dev.ijskoud.appiecal.ah.rooster.shift.ShiftTeams
import java.util.*

class Service {
    private val repository: Repository = Repository()

    companion object {
        private var instance: Service? = null

        fun getInstance(): Service {
            if (instance == null) {
                instance = Service()
            }

            return instance!!
        }
    }

    /**
     * Fetches the rooster
     * @param startDate The date the data should begin
     */
    suspend fun getRooster(startDate: Date? = null): List<Shift> {
        val rooster = repository.getRooster(startDate) ?: return emptyList()
        return rooster.data.scheduleByWeek.map { schedule -> convertToShift(schedule) }
    }

    /**
     * Converts schedule date into a Shift
     * @param schedule The schedule data to convert
     * @return Shift
     */
    private fun convertToShift(schedule: Schedule): Shift {
        val startDate = Date.from(Utils.convertToInstant(schedule.startTime))
        val endDate = Date.from(Utils.convertToInstant(schedule.endTime))

        // Remove the location name from the name (e.g. Hoofdkantoor ZAANDAM -> Hoofdkantoor)
        val storeName = schedule.store.abbreviatedDisplayName.replace(schedule.store.location, "")
        val teamNames = schedule.teamNames.map { teamName ->
            ShiftTeams.entries.find { team -> team.team == teamName }!!
        }

        return Shift(
            startDate = startDate,
            endDate = endDate,
            minutes = schedule.minutes,
            paidMinutes = schedule.paidMinutes,
            sickMinutes = schedule.sickMinutes,
            leaveMinutes = schedule.leaveMinutes,
            teamNames = teamNames,
            storeName = storeName.trim(),
            storeId = schedule.storeId.trim(),
        )
    }
}