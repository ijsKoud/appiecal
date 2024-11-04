package dev.ijskoud.appiecal.api

import com.google.gson.Gson
import dev.ijskoud.appiecal.ah.authentication.AuthenticationService
import dev.ijskoud.appiecal.ah.rooster.Service
import dev.ijskoud.appiecal.calendar.Calendar
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled

@org.springframework.stereotype.Service
class Service {
    private val logger = LoggerFactory.getLogger(Service::class.java)
    private val authenticationService = AuthenticationService.getInstance()
    private val rooster: Service = Service.getInstance()
    private val calendar: Calendar = Calendar.getInstance()

    /**
     * Syncs the calendar periodically
     */
    @Scheduled(cron = "0 0 * * * *") // run every hour
    suspend fun syncCalendar() {
        calendar.sync()
    }

    suspend fun getAuthorization(code: String) {
        val token = authenticationService.getAuthorization(code)
        authenticationService.updateToken(token)

        logger.info("Reauthorization with code: $code")
    }

    suspend fun getRawCalendar(): String {
        val events = calendar.store.get()
        return Gson().toJson(events)
    }

    suspend fun getFetchedCalendar(): String {
        val events = rooster.getRooster()
        return Gson().toJson(events)
    }

    fun getAuthorizationUrl(): String {
        return authenticationService.getAuthorizationUrl()
    }
}