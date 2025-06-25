package nl.klrnbk.daan.appiecal.apps.schedule.helpers

import nl.klrnbk.daan.appiecal.apps.schedule.constants.AH_TIME_ZONE
import nl.klrnbk.daan.appiecal.apps.schedule.constants.DATE_TIME_FORMATTER
import java.time.LocalDateTime
import java.time.ZonedDateTime

fun localDateTimeToZonedDateTime(dateTime: LocalDateTime): ZonedDateTime = dateTime.atZone(AH_TIME_ZONE)

fun localDateTimeStringToZonedDateTime(dateTime: String): ZonedDateTime {
    val localDateTime = LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER)
    return localDateTimeToZonedDateTime(localDateTime)
}
