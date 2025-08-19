package nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule

import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.ShiftDepartment
import java.time.ZonedDateTime
import java.util.UUID

data class ScheduleResponse<T : ScheduleResponseShift<*>>(
    val shifts: List<T>,
) {
    val amount: Int = shifts.size

    companion object {
        const val SCHEDULE_RESPONSE_EXAMPLE = """
            {
              "amount": 1,
              "shifts": [
                {
                  "id": "fcb42be0-7988-4ae8-83af-1d453b0a423e",
                  "storeId": "1148",
                  "startDate": "2025-05-18T20:00:00",
                  "endDate": "2025-05-18T23:00:00",
                  "createdAt": "2025-05-18T20:00:00",
                  "updatedAt": "2025-05-18T20:00:00",
                  "departments": [
                    "Operatie"
                  ],
                  "activities": [
                    {
                      "id": "44a275cb-fb32-41c1-ad22-2c522028e6bc",
                      "description": "Promotie",
                      "startDate": "2025-05-18T20:00:00",
                      "endDate": "2025-05-18T23:00:00",
                      "createdAt": "2025-05-18T20:00:00",
                      "updatedAt": "2025-05-18T20:00:00",
                      "department": "Operatie",
                      "timeCode": "WRK",
                      "paid": true
                    }
                  ]
                }
              ]
            }
        """
    }
}

open class ScheduleResponseShift<T : ScheduleResponseShiftActivity>(
    val id: UUID,
    val storeId: String,
    val startDate: ZonedDateTime,
    val endDate: ZonedDateTime,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val departments: List<ShiftDepartment>,
    val activities: MutableList<T>,
) {
    fun isFullSickDay(): Boolean = activities.size == 1 && activities.first().description === "Ziek"

    fun isFullAbsentDay(): Boolean = activities.size == 1 && activities.first().timeCode == "ABSENCE AH"
}

open class ScheduleResponseShiftActivity(
    val id: UUID,
    val description: String,
    val startDate: ZonedDateTime,
    val endDate: ZonedDateTime,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val department: ShiftDepartment,
    val timeCode: String,
    val paid: Boolean,
)
