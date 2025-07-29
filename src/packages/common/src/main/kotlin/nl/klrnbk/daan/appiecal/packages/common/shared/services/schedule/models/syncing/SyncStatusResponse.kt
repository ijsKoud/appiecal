package nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.syncing

data class SyncStatusResponse(
    val shifts: SyncStatusResponseEntries,
)

data class SyncStatusResponseEntries(
    val new: List<String>,
    val update: List<String>,
    val delete: List<String>,
)
