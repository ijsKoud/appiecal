package nl.klrnbk.daan.appiecal.packages.common.shared.services.calendar.models

import java.time.ZonedDateTime

data class CreateEventRequestBody(
    val eventId: String,
    val title: String,
    val description: String,
    val location: String,
    val startDate: ZonedDateTime,
    val endDate: ZonedDateTime,
)
