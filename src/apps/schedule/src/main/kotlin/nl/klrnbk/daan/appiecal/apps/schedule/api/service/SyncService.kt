package nl.klrnbk.daan.appiecal.apps.schedule.api.service

import nl.klrnbk.daan.appiecal.apps.schedule.api.models.syncing.SyncStatusResponse
import nl.klrnbk.daan.appiecal.apps.schedule.api.models.syncing.SyncStatusResponseEntries
import nl.klrnbk.daan.appiecal.apps.schedule.datasource.models.ActivityModel
import nl.klrnbk.daan.appiecal.apps.schedule.datasource.models.ShiftModel
import nl.klrnbk.daan.appiecal.apps.schedule.helpers.models.GetUpdatableShiftsResult
import nl.klrnbk.daan.appiecal.apps.schedule.helpers.models.SplitCrudResult
import nl.klrnbk.daan.appiecal.apps.schedule.helpers.models.SplitShiftsToCrudResult
import org.springframework.stereotype.Service

@Service
class SyncService {
    fun resultsToResponse(results: SplitShiftsToCrudResult): SyncStatusResponse {
        val shiftsResponse =
            SyncStatusResponseEntries(
                new = results.shifts.new.mapNotNull(ShiftModel::id),
                update = results.shifts.update.mapNotNull(ShiftModel::id),
                delete = results.shifts.delete.mapNotNull(ShiftModel::id),
            )

        return SyncStatusResponse(
            shifts = shiftsResponse,
        )
    }

    fun splitShiftsToCrud(
        dbShifts: List<ShiftModel>,
        apiShifts: List<ShiftModel>,
    ): SplitShiftsToCrudResult {
        val deletableShifts = dbShifts.filter { !apiShifts.any(it::isDateEqual) }
        val (restApiShifts, newShifts) = apiShifts.partition { dbShifts.any(it::isDateEqual) }
        val updatableShifts = getUpdatableShifts(dbShifts, restApiShifts)
        val shiftCrudResult =
            SplitCrudResult(
                deletableShifts.toMutableList(),
                newShifts.toMutableList(),
                updatableShifts.shifts.toMutableList(),
            )

        return SplitShiftsToCrudResult(shiftCrudResult, updatableShifts.activities)
    }

    private fun getUpdatableShifts(
        dbShifts: List<ShiftModel>,
        apiShifts: List<ShiftModel>,
    ): GetUpdatableShiftsResult {
        val shiftPairs =
            apiShifts.mapNotNull { shift ->
                val dbShift = dbShifts.find(shift::isDateEqual)
                if (dbShift == null) return@mapNotNull null

                shift.id = dbShift.id
                Pair(dbShift, shift)
            }

        val updatableShifts =
            shiftPairs
                .filter { (dbShift, apiShift) ->
                    dbShift.equals(
                        apiShift,
                    )
                }.map(Pair<ShiftModel, ShiftModel>::second)
                .toMutableList()

        val crudResults =
            shiftPairs
                .map { (dbShift, apiShift) ->
                    val (shouldUpdateShift, result) = splitActivitiesToCrud(dbShift.activities, apiShift.activities)
                    if (shouldUpdateShift) updatableShifts.add(apiShift)
                    result
                }.reduceOrNull { acc, result -> acc.mergeFrom(result) }
                ?: SplitCrudResult(mutableListOf(), mutableListOf(), mutableListOf())

        return GetUpdatableShiftsResult(crudResults, updatableShifts)
    }

    private fun splitActivitiesToCrud(
        dbActivities: List<ActivityModel>,
        apiActivities: List<ActivityModel>,
    ): Pair<Boolean, SplitCrudResult<ActivityModel>> {
        val deletableActivities = dbActivities.filter { !apiActivities.any(it::isDateTimeEqual) }
        val (restApiActivities, newActivities) = apiActivities.partition { dbActivities.any(it::isDateTimeEqual) }

        val updatableActivities =
            restApiActivities.filter { activity ->
                val dbActivity = dbActivities.find(activity::isDateTimeEqual)
                if (dbActivity == null) return@filter false

                activity.id = dbActivity.id
                dbActivity.equals(activity)
            }

        val shouldUpdateShift = newActivities.isNotEmpty() || updatableActivities.isNotEmpty()
        val result =
            SplitCrudResult(
                deletableActivities.toMutableList(),
                newActivities.toMutableList(),
                updatableActivities.toMutableList(),
            )

        return Pair(shouldUpdateShift, result)
    }
}
