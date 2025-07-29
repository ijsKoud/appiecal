package nl.klrnbk.daan.appiecal.apps.schedule.helpers

import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleActivity
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleShift

fun getUniqueStoreIds(shifts: List<ScheduleShift>): Set<String> = shifts.map(ScheduleShift::storeId).toSet()

fun isActivityPartOfShift(
    shift: ScheduleShift,
    activity: ScheduleActivity,
): Boolean {
    val shiftRange = shift.startDate..shift.endDate
    return activity.startDate in shiftRange && activity.endDate in shiftRange
}
