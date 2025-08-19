package nl.klrnbk.daan.appiecal.apps.sync.helpers

import nl.klrnbk.daan.appiecal.apps.sync.api.models.store.StoreInformation
import nl.klrnbk.daan.appiecal.packages.common.constants.AH_TIME_ZONE
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponseShift
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponseShiftActivity

fun getEventDescriptionFromShift(
    shift: ScheduleResponseShift<ScheduleResponseShiftActivity>,
    storeInformation: StoreInformation,
): String {
    val (activities, breaks) = shift.activities.partition { it.timeCode == "WRK" }
    val shiftDetails =
        """
        === Shift Details ===
        Store: ${storeInformation.name} (${shift.storeId})
        Address: ${storeInformation.address}
        
        Departments: ${shift.departments.joinToString(", ") { it.name }}
        Amount of breaks: ${breaks.size}
        """.trimIndent()

    val activitiesListString = activities.joinToString("\n", transform = ::getEventDescriptionForActivity)
    val breaksListString = breaks.joinToString("\n", transform = ::getEventDescriptionForBreaks)

    return "$shiftDetails\n\n===Activities===\n${activitiesListString}\n\n===Breaks===\n$breaksListString"
}

fun getEventDescriptionForActivity(activity: ScheduleResponseShiftActivity): String {
    val startTime = activity.startDate.withZoneSameInstant(AH_TIME_ZONE).toLocalTime()
    val endTime = activity.endDate.withZoneSameInstant(AH_TIME_ZONE).toLocalTime()

    return "- ${activity.description} ($startTime-$endTime)"
}

fun getEventDescriptionForBreaks(activity: ScheduleResponseShiftActivity): String {
    val startTime = activity.startDate.withZoneSameInstant(AH_TIME_ZONE).toLocalTime()
    val endTime = activity.endDate.withZoneSameInstant(AH_TIME_ZONE).toLocalTime()

    return "- ${if (activity.paid) "Normal break [PAID]" else "Meal break [UNPAID]"} ($startTime-$endTime)"
}
