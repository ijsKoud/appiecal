package dev.ijskoud.appiecal.ah.rooster

import java.util.Date
import java.util.UUID

/**
 * All Albert Heijn supermarket teams (does not include AH To Go, DC or Online)
 */
enum class Teams(val team: String) {
    Management("MANAGEMENT"),
    OperatieGekoeld("OPERATIE GEKOELD"),
    Operatie("OPERATIE"),
    Service("SERVICE"),
    ServiceIndirect("SERVICE INDIRECT"),
    Vers("VERS"),
}

/**
 * Class containing the Albert Heijn rooster information
 */
class Event(
    public val startDate: Date,
    public val endDate: Date,
    public val minutes: Int,
    public val paidMinutes: Int,
    public val sickMinutes: Int,
    public val leaveMinutes: Int,
    public val teamNames: List<Teams>,
    public val storeName: String,
    public var id: UUID = UUID.randomUUID(),
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
        return this.sickMinutes > 0;
    }

    /**
     * Checks whether the selected day is registered as a leave day
     */
    fun isLeaveDay(): Boolean {
        return this.leaveMinutes > 0;
    }

    /**
     * Checks whether the other event is equal to this event
     * @param strict Whether to strictly check the dates, if false it will only check the date and not the duration
     */
    fun isEqual(other: Event, strict: Boolean = false): Boolean {
        // If not strict, only the date should be the same, not the exact duration
        val isDateEqual = this.startDate == other.startDate || this.endDate == other.endDate;
        val isDateStrictEqual = this.startDate === other.startDate || this.endDate == other.endDate;

        val isStoreEqual = this.storeName == other.storeName;
        val areTeamNamesEqual = teamNames == other.teamNames;

        // strict ? isDateStrictEqual : isDateEqual
        return (if (strict) isDateStrictEqual else isDateEqual) && isStoreEqual && areTeamNamesEqual;
    }

    /**
     * Merges the two events together
     * @param event The event to merge
     */
    fun mergeEvent(event: Event): Event {
        event.id = id;
        return event;
    }

    /**
     * Checks whether the event is older than the check window
     * @param date The start of the check window
     */
    fun isOldEvent(date: Date): Boolean {
        return this.startDate.before(date);
    }
}