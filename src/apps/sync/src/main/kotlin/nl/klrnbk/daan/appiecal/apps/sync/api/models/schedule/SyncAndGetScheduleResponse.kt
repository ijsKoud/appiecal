package nl.klrnbk.daan.appiecal.apps.sync.api.models.schedule

import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponseShift
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponseShiftActivity

data class SyncAndGetScheduleResponse(
    val new: List<ScheduleResponseShift<ScheduleResponseShiftActivity>>,
    val updated: List<ScheduleResponseShift<ScheduleResponseShiftActivity>>,
    val deleted: List<String>,
) {
    override fun toString() = "new=${new.size}; updated=${updated.size}; deleted=${deleted.size}"
}
