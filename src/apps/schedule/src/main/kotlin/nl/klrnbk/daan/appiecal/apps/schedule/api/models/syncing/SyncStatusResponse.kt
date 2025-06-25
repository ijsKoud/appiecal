package nl.klrnbk.daan.appiecal.apps.schedule.api.models.syncing

data class SyncStatusResponse(
    val shifts: SyncStatusResponseEntries,
)

data class SyncStatusResponseEntries(
    val new: List<String>,
    val update: List<String>,
    val delete: List<String>,
)
