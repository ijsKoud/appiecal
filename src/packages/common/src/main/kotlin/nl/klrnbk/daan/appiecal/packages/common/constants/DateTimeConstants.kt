package nl.klrnbk.daan.appiecal.packages.common.constants

import java.time.ZoneId
import java.time.format.DateTimeFormatter

const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
const val DATE_TIME_ZONE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssX"
val AH_TIME_ZONE: ZoneId = ZoneId.of("Europe/Amsterdam")

val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
