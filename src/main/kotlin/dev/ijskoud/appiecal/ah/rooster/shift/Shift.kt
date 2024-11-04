package dev.ijskoud.appiecal.ah.rooster.shift

import biweekly.ICalendar
import biweekly.component.VEvent
import dev.ijskoud.appiecal.ah.rooster.Utils
import java.time.Duration
import java.time.Instant
import java.util.*

/**
 * Class containing the Albert Heijn rooster information
 */
class Shift(
    val startDate: Date,
    val endDate: Date,
    val minutes: Int,
    val paidMinutes: Int,
    val sickMinutes: Int,
    val leaveMinutes: Int,
    val teamNames: List<ShiftTeams>,
    val storeName: String,
    val storeId: String,
    var id: UUID = UUID.randomUUID(),
    private var lastModifiedDate: Date = Date.from(Instant.now()),
    private var createdAtDate: Date = Date.from(Instant.now())
) {

    /**
     * Gets the first team name on the list of team names
     */
    val teamName: String
        get() = teamNames.first().name

    /**
     * Checks whether the selected day is registered as a sick day
     */
    fun isSickDay(): Boolean {
        return this.sickMinutes > 0
    }

    /**
     * Checks whether the selected day is registered as a leave day
     */
    fun isLeaveDay(): Boolean {
        return this.leaveMinutes > 0
    }

    /**
     * Checks whether the other event is equal to this event
     * @param strict Whether to strictly check the dates, if false it will only check the date and not the duration
     */
    fun isEqual(other: Shift, strict: Boolean = false): Boolean {
        // If not strict, only the date should be the same, not the exact duration
        val isDateEqual = this.startDate == other.startDate || this.endDate == other.endDate
        val isDateStrictEqual = this.startDate === other.startDate || this.endDate == other.endDate

        val isStoreEqual = this.storeName == other.storeName
        val areTeamNamesEqual = teamNames == other.teamNames

        // strict ? isDateStrictEqual : isDateEqual
        return (if (strict) isDateStrictEqual else isDateEqual) && isStoreEqual && areTeamNamesEqual
    }

    /**
     * Checks if the provided VEvent is equal to this shift
     * @param other The VEvent data you want to check
     * @return Whether the event data is the same
     */
    fun isVEventEqual(other: VEvent): Boolean {
        return (other.uid.value.equals(id.toString())
                && other.dateStart.value.equals(startDate)
                && other.dateEnd.value.equals(endDate)
                && other.summary.value.equals(getSummary())
                && other.description.value.equals(getDescription()))
    }

    /**
     * Merges the two events together
     * @param shift The shift to merge
     */
    fun mergeEvent(shift: Shift): Shift {
        shift.id = id
        shift.lastModifiedDate = Date.from(Instant.now())
        shift.createdAtDate = createdAtDate

        return shift
    }

    /**
     * Checks whether the event is older than the check window
     * @param date The start of the check window
     */
    fun isOldEvent(date: Date): Boolean {
        return this.startDate.before(date)
    }

    /**
     * Generates an event description for the shift
     */
    fun getDescription(): String {
        return """
            === Informatie ===
            Winkel: $storeName - $storeId
            Afdelingen: ${teamNames.joinToString(", ")}
            
            === Uren registratie ===
            Betaald: ${Utils.getTimeString((paidMinutes * 60 * 1e3).toLong())}
            Onbetaald: ${Utils.getTimeString(getUnpaidTime())}
            Totaal: ${Utils.getTimeString(getShiftDuration())}
        """.trimIndent()
    }

    /**
     * Returns the shift duration in milliseconds
     */
    fun getShiftDuration(): Long {
        return Duration.between(startDate.toInstant(), endDate.toInstant()).toMillis()
    }

    /**
     * Returns the unpaid shift time
     */
    fun getUnpaidTime(): Long {
        return getShiftDuration() - (paidMinutes * 60 * 1e3).toLong()
    }

    /**
     * Returns the event summary
     */
    fun getSummary(): String {
        if (teamNames.size > 1) {
            return "Shift op meerdere afdelingen"
        }

        return "Shift op $teamName"
    }


    /**
     * Returns the Shift ical body
     */
    fun toIcal(): String {
        val ical = ICalendar()
        val event = VEvent()
        event.setSummary(getSummary())
        event.setDescription(getDescription())
        event.setUid(id.toString())
        event.setDateStart(startDate)
        event.setDateEnd(endDate)
        event.setCreated(createdAtDate)
        event.setLastModified(lastModifiedDate)

        ical.addEvent(event)
        ical.setProductId("//AppieCal//1.0.0")
        return ical.write()
    }

    override fun toString(): String {
        return """
            id=$id
            minutes=$minutes
            paidMinutes=$paidMinutes
            sickMinutes=$sickMinutes
            leaveMinutes=$leaveMinutes
            storeName=$storeName
            storeId=$storeId
            startDate=$startDate
            endDate=$endDate
            summary=${getSummary()}
            description=${getDescription()}
        """.trimIndent()
    }
}