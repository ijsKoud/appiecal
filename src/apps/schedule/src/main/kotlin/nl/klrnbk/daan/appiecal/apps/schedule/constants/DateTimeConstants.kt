package nl.klrnbk.daan.appiecal.apps.schedule.constants

import java.time.format.DateTimeFormatter

const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
