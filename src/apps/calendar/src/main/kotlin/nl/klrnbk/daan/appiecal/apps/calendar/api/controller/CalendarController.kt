package nl.klrnbk.daan.appiecal.apps.calendar.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import nl.klrnbk.daan.appiecal.apps.calendar.api.facade.CalendarFacade
import nl.klrnbk.daan.appiecal.apps.calendar.api.models.CalendarListEntryResponse
import nl.klrnbk.daan.appiecal.packages.common.responses.error.BaseErrorResponses
import nl.klrnbk.daan.appiecal.packages.security.idp.models.JwtAuthenticationToken
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@SecurityRequirement(name = "api-key")
@RequestMapping("/v1/calendar")
@BaseErrorResponses
class CalendarController(
    private val calendarFacade: CalendarFacade,
) {
    @GetMapping("/list")
    @Operation(summary = "Gets a list of available calendars")
    @ApiResponse(
        responseCode = "200",
        description = "List of calendars",
        content = [
            Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = CalendarListEntryResponse::class)),
                examples = [ExampleObject("[{\"name\":\"Work Schedule\",\"href\":\"/calendars/work-schedule\"}]")],
            ),
        ],
    )
    @PreAuthorize("@scopes.hasScope(authentication, 'https://klrnbk.nl/projects/appiecal:use')")
    fun getListOfCalendars(authentication: JwtAuthenticationToken) = calendarFacade.getCalendarList(authentication.principal)

    @PostMapping("/set")
    @Operation(summary = "Sets the user's linked calendar ")
    @ApiResponse(
        responseCode = "202",
        description = "Linked calendar has been updated",
        content = [
            Content(
                mediaType = "*/*",
                schema = Schema(implementation = Void::class),
            ),
        ],
    )
    @PreAuthorize("@scopes.hasScope(authentication, 'https://klrnbk.nl/projects/appiecal:use')")
    fun setOrUpdateCalendar(
        authentication: JwtAuthenticationToken,
        @RequestParam("href", required = true) href: String,
    ) = calendarFacade.setOrUpdateCalendarUrl(authentication.principal, href)
}
