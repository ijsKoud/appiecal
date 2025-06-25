package nl.klrnbk.daan.appiecal.apps.schedule.helpers.models

import nl.klrnbk.daan.appiecal.apps.schedule.datasource.models.ActivityModel
import nl.klrnbk.daan.appiecal.apps.schedule.datasource.models.ShiftModel

data class SplitShiftsToCrudResult(
    val shifts: SplitCrudResult<ShiftModel>,
    val activities: SplitCrudResult<ActivityModel>,
)

data class GetUpdatableShiftsResult(
    val activities: SplitCrudResult<ActivityModel>,
    val shifts: List<ShiftModel>,
)

data class SplitCrudResult<T>(
    val delete: MutableList<T>,
    val new: MutableList<T>,
    val update: MutableList<T>,
) {
    fun mergeFrom(other: SplitCrudResult<T>): SplitCrudResult<T> {
        delete.addAll(other.delete)
        update.addAll(other.update)
        new.addAll(other.new)

        return this
    }

    override fun toString(): String = "delete=${delete.size}; update=${update.size}; new=${new.size}"
}
