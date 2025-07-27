package nl.klrnbk.daan.appiecal.apps.calendar.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import nl.klrnbk.daan.appiecal.apps.calendar.api.facade.EventFacade
import nl.klrnbk.daan.appiecal.packages.common.responses.error.BaseErrorResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@SecurityRequirement(name = "api-key")
@RequestMapping("/v1/events")
@BaseErrorResponses
class EventController(
    private val eventFacade: EventFacade,
) {
    @DeleteMapping("/{userId}/delete")
    @Operation(summary = "Deletes an event from the calendar (SERVICE ACCOUNT ONLY)")
    @ApiResponse(
        responseCode = "204",
        description = "Event is deleted",
        content = [
            Content(
                mediaType = "*/*",
                schema = Schema(implementation = Void::class),
            ),
        ],
    )
    @PreAuthorize("@scopes.hasScope(authentication, 'https://klrnbk.nl/projects/appiecal:use') && @scopes.isServiceAccount(authentication)")
    fun deleteEvent(
        @PathVariable userId: String,
        @RequestParam("event-id") eventId: String,
    ) = eventFacade.deleteEvent(userId, eventId)

    fun createEvent(): ResponseEntity<Void> = ResponseEntity(HttpStatus.CREATED)

    fun updateEvent(): ResponseEntity<Void> = ResponseEntity(HttpStatus.NO_CONTENT)
}
