package nl.klrnbk.daan.appiecal.apps.schedule.api.models.schedule

data class ScheduleResponse(
    val shifts: List<ScheduleShift>,
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
