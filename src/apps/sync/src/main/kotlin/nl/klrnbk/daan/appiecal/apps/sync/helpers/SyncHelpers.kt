package nl.klrnbk.daan.appiecal.apps.sync.helpers

import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponseShift
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponseShiftActivity

fun getStoreIdsFromShifts(shifts: List<ScheduleResponseShift<ScheduleResponseShiftActivity>>): List<String> =
    shifts
        .map {
            it.storeId
        }.toSet()
        .toList()
