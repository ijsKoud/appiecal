package dev.ijskoud.appiecal.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {
    private val service: Service = Service()

    @GetMapping("/authorize")
    suspend fun getAuthorization(@RequestParam("code") code: String?) {
        if (code.isNullOrEmpty()) {
            ResponseEntity.badRequest().body("Query parameter 'code' must not be null or empty.")
            return
        }

        service.getAuthorization(code)
        ResponseEntity.ok()
    }

    @GetMapping("/authenticate")
    fun getAuthorizationUrl(): String {
        return service.getAuthorizationUrl()
    }

    @GetMapping(path = ["/calendar/cached"], produces = ["application/json"])
    @ResponseBody
    suspend fun getRawCalendar(): String {
        return service.getRawCalendar()
    }

    @GetMapping(path = ["/calendar/fetched"], produces = ["application/json"])
    @ResponseBody
    suspend fun getFetchedCalendar(): String? {
        return service.getFetchedCalendar()
    }

    @GetMapping(path = ["/calendar/sync"], produces = ["application/json"])
    @ResponseBody
    suspend fun getSyncCalendar(): ResponseEntity<String> {
        service.syncCalendar()
        return ResponseEntity.ok("Syncing complete")
    }

    @GetMapping(path = ["/ping"], produces = ["application/json"])
    @ResponseBody
    suspend fun getPing(): String {
        return "pong"
    }
}