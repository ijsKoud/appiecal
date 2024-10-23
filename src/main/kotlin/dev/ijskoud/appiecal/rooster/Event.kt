package dev.ijskoud.appiecal.rooster

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
    public val storeName: String
) {
    public val id: UUID = UUID.randomUUID()

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
}