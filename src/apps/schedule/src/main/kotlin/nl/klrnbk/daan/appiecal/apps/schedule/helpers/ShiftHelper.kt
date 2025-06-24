package nl.klrnbk.daan.appiecal.apps.schedule.helpers

import nl.klrnbk.daan.appiecal.apps.schedule.api.responses.schedule.ScheduleActivity
import nl.klrnbk.daan.appiecal.apps.schedule.api.responses.schedule.ScheduleShift

fun getUniqueStoreIds(shifts: List<ScheduleShift>): Set<String> = shifts.map(ScheduleShift::storeId).toSet()

fun isActivityPartOfShift(
    shift: ScheduleShift,
    activity: ScheduleActivity,
): Boolean {
    val shiftRange = shift.startDate..shift.endDate
    return activity.startDate in shiftRange && activity.endDate in shiftRange
}
