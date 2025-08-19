package nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models

enum class ShiftDepartment {
    Management,
    Operatie,
    OperatieGekoeld,
    OperatieKwaliteit,
    Service,
    ServiceIndirect,
    ServiceZelfScan,
    ServiceBalie,
    Vers,
    VersFoodServices,
    Unknown,
    ;

    override fun toString(): String = name.split("(?<=[a-z])(?=[A-Z])").joinToString(" ")

    companion object {
        fun getFromGqlResponse(type: String): ShiftDepartment =
            when (type) {
                "MANAGEMENT" -> Management
                "OPERATIE" -> Operatie
                "OPERATIE GEKOELD" -> OperatieGekoeld
                "OPERATIE KWALITEIT" -> OperatieKwaliteit
                "SERVICE" -> Service
                "SERVICE INDIRECT" -> ServiceIndirect
                "SERVICE ZELFSCAN" -> ServiceZelfScan
                "SERVICEBALIE" -> ServiceBalie
                "VERS" -> Vers
                "VERS FOODSERVICES" -> VersFoodServices
                else -> Unknown
            }
    }
}
