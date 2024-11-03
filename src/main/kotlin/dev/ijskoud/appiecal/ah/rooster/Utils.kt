package dev.ijskoud.appiecal.ah.rooster

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class Utils {
    companion object {
        /**
         * Converts a Long into a duration string
         * @param time The time to convert into a string
         * @return A duration string (e.g. 1 hour, 30 minutes, 10 seconds)
         */
        fun getTimeString(time: Long): String {
            var millis = time
            val hours = TimeUnit.MILLISECONDS.toHours(millis)
            millis -= TimeUnit.HOURS.toMillis(hours)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
            millis -= TimeUnit.MINUTES.toMillis(minutes)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(millis)

            val duration = StringBuilder()
            if (hours > 0) {
                duration.append(hours).append(" hour").append(if (hours > 1) "s" else "").append(", ")
            }

            if (minutes > 0) {
                duration.append(minutes).append(" min").append(if (minutes > 1) "s" else "").append(", ")
            }

            if (seconds > 0 || duration.isEmpty()) { // Show seconds if no other unit exists
                duration.append(seconds).append(" second").append(if (seconds > 1) "s" else "")
            }

            // Remove trailing comma and space if they exist
            val length = duration.length
            if (length > 2 && duration.substring(length - 2) == ", ") {
                duration.setLength(length - 2)
            }

            return duration.toString()
        }

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
    }
}