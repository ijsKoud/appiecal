package nl.klrnbk.daan.appiecal.apps.calendar.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import nl.klrnbk.daan.appiecal.apps.calendar.api.facade.EventFacade
import nl.klrnbk.daan.appiecal.apps.calendar.constants.CREATE_EVENT_BODY
import nl.klrnbk.daan.appiecal.packages.common.responses.error.BaseErrorResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.parameters.RequestBody as SwaggerRequestBody

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

    @PutMapping("/{userId}", consumes = ["text/plain"])
    @Operation(summary = "Creates or updated an event on the calendar (SERVICE ACCOUNT ONLY)")
    @ApiResponse(
        responseCode = "200",
        description = "Event is created/updated",
        content = [
            Content(
                mediaType = "text/plain",
                schema = Schema(implementation = String::class),
                examples = [ExampleObject("d6db162a-86dc-43d6-ac66-9f3657dae0dd")],
            ),
        ],
    )
    fun putEvent(
        @PathVariable
        userId: String,
        @RequestBody
        @SwaggerRequestBody(
            description = "Body containing VEVENT data",
            content = [
                Content(examples = [ExampleObject(CREATE_EVENT_BODY)]),
            ],
        )
        content: String,
    ): String = eventFacade.putEvent(userId, content)
}
