package dev.ijskoud.appiecal.ah.rooster

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


/**
 * Get the date range for the current week
 * @param startDate The start date of the week (optional)
 * @return A Pair of Dates for the start and end of the week
 */
fun getDateRange(startDate: Date? = null): Pair<Date, Date> {
    val calendar = Calendar.getInstance()

    // Use the provided startDate or the current date if startDate is null
    if (startDate != null) {
        calendar.time = startDate
    }

    // If it's Sunday (Calendar.SUNDAY is 1), adjust to the previous week
    if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
    }

    // Calculate the start of the week (Monday)
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    calendar.set(Calendar.HOUR_OF_DAY, 2) // Offset date by 2 hours to prevent date from moving to previous day
    val startOfWeek = calendar.time

    // Calculate the end of the week (Sunday)
    calendar.add(Calendar.DAY_OF_WEEK, 7)
    calendar.set(Calendar.HOUR_OF_DAY, 2) // Offset date by 2 hours to prevent date from moving to previous day
    val endOfWeek = calendar.time

    return Pair(startOfWeek, endOfWeek)
}

/**
 * Converts date string to an instant
 * @param dateString The date as string
 * @return an Instant
 */
fun convertToInstant(dateString: String): Instant {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val localDateTime = LocalDateTime.parse(dateString, formatter)

    return localDateTime.atZone(ZoneId.systemDefault()).toInstant()
}