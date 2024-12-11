package dev.ijskoud.appiecal.calendar

import dev.ijskoud.appiecal.ah.rooster.shift.Shift
import dev.ijskoud.appiecal.ah.rooster.Service
import dev.ijskoud.appiecal.ah.rooster.Utils
import dev.ijskoud.appiecal.store.calendar.CalendarStore
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


class Calendar {
    private val rooster: Service = Service.getInstance()
    private val logger = LoggerFactory.getLogger(CalendarStore::class.java)

    val store: CalendarStore = CalendarStore.getInstance()

    companion object {
        private var instance: Calendar? = null

        fun getInstance(): Calendar {
            if (instance == null) {
                instance = Calendar()
            }

            return instance!!
        }
    }

    suspend fun sync() {
        try {
            val dates = getDates()
            val existingEvents = store.get()
            val pulledEvents = dates
                .map { date -> rooster.getRooster(date) ?: throw Exception("No rooster found for date: $date") }
                .reduce { acc, events -> acc + events }


            val mergedEvents = mergeEvents(existingEvents.toList(), pulledEvents)
            store.update((mergedEvents.first).toTypedArray())
            update(dates.first(), mergedEvents.first, mergedEvents.second)
        } catch (e: Exception) {
            logger.error("Failed to update the schedule - ${e.message}")
        }
    }

    /**
     * Updates the shifts on caldav
     * @param date A date in the first week
     * @param shifts The shifts that should be updated
     * @param unMatchedShifts Shifts that should be deleted
     */
    private fun update(date: Date, shifts: List<Shift>, unMatchedShifts: List<Shift>) {
        val dates = Utils.getDateRange(date)
        val caldav = CalDav()

        // Delete shifts that are no longer available
        val deletableEvents = unMatchedShifts.filter { event -> !event.isOldEvent(dates.first) }
        deletableEvents.parallelStream().forEach { event -> caldav.deleteEvent(event.id.toString()) }


        // Update event s only if event on caldav server is outdated
        val caldavEvents = caldav.getEvents(shifts.map { it.id.toString() }.toTypedArray())
        var updated = 0

        shifts.stream().forEach { shift ->
            val caldavEvent = caldavEvents.find { ev -> ev.uid.value.equals(shift.id.toString()) }
            if (caldavEvent != null && shift.isVEventEqual(caldavEvent)) {
                logger.debug("Skipped update for following event because information are up-to-date:\n{}", shift)
                return@forEach // breaks the loop
            }

            try {
                caldav.putEvent(shift)
                updated++
            } catch (e: Exception) {
                logger.error(e.message, e)
            }
        }

        logger.info("Syncing completed - checked=${shifts.size + unMatchedShifts.size}, updated=$updated, deleted=${deletableEvents.size}")
    }

    /**
     * Merges the existing events with newly pulled events
     * @param existingShifts The existing events
     * @param pulledShifts The events that have been pulled from the API
     * @return A list of updated events and a list of deleted events
     */
    private fun mergeEvents(existingShifts: List<Shift>, pulledShifts: List<Shift>): Pair<List<Shift>, List<Shift>> {
        val matchedShifts = pulledShifts
            .map { event ->
                existingShifts.find<Shift> { existing -> existing.isEqual(event) }?.mergeEvent(event)
                event
            }

        val deletableEvents = existingShifts.filter { event -> matchedShifts.none { existing -> existing.isEqual(event) } }
        return Pair(matchedShifts, deletableEvents)
    }

    private fun getDates(): Array<Date> {
        val todayLocal = LocalDate.now()
        val nextWeekLocal = LocalDate.now().plusWeeks(1)
        val weekAfterLocal = LocalDate.now().plusWeeks(2)

        val today = Date.from(todayLocal.atStartOfDay(ZoneId.systemDefault()).toInstant())
        val nextWeek = Date.from(nextWeekLocal.atStartOfDay(ZoneId.systemDefault()).toInstant())
        val weekAfter = Date.from(weekAfterLocal.atStartOfDay(ZoneId.systemDefault()).toInstant())

        return arrayOf(today, nextWeek, weekAfter)
    }
}