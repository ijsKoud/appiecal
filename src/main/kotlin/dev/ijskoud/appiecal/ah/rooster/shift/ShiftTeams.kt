package dev.ijskoud.appiecal.ah.rooster.shift

/**
 * All Albert Heijn supermarket teams (does not include AH To Go, DC or Online)
 */
enum class ShiftTeams(val team: String) {
    Management("MANAGEMENT"),
    OperatieGekoeld("OPERATIE GEKOELD"),
    Operatie("OPERATIE"),
    Service("SERVICE"),
    ServiceIndirect("SERVICE INDIRECT"),
    Vers("VERS"),
}