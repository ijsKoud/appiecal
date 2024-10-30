package dev.ijskoud.appiecal.calendar

import dev.ijskoud.appiecal.ah.rooster.Event
import dev.ijskoud.appiecal.ah.rooster.RoosterService
import dev.ijskoud.appiecal.ah.rooster.getDateRange
import dev.ijskoud.appiecal.store.calendar.CalendarStore
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class Calendar {
    private val rooster: RoosterService = RoosterService.getInstance()
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
        val dates = getDates()
        val existingEvents = store.get()
        val pulledEvents = dates
            .map { date -> rooster.getRooster(date) }
            .reduce { acc, events -> acc + events }


        val mergedEvents = mergeEvents(existingEvents.toList(), pulledEvents)
        store.update((mergedEvents.first).toTypedArray())
        update(dates.first(), mergedEvents.first, mergedEvents.second)
    }

    private fun update(date: Date, events: List<Event>, unMatchedEvents: List<Event>) {
        val dates = getDateRange(date)
        val deletableEvents = unMatchedEvents.filter { event -> !event.isOldEvent(dates.first) }

        // TODO: update calendar
        println(deletableEvents.map { it.startDate.toString() })
    }

    /**
     * Merges the existing events with newly pulled events
     * @param existingEvents The existing events
     * @param pulledEvents The events that have been pulled from the API
     * @return A list of updated events and a list of deleted events
     */
    private fun mergeEvents(existingEvents: List<Event>, pulledEvents: List<Event>): Pair<List<Event>, List<Event>> {
        val matchedEvents = pulledEvents
            .map { event ->
                existingEvents.find<Event> { existing -> existing.isEqual(event) }?.mergeEvent(event)
                event
            }

        val deletableEvents = existingEvents.filter { event -> matchedEvents.none { existing -> existing.isEqual(event) } }
        return Pair(matchedEvents, deletableEvents)
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