package nl.klrnbk.daan.appiecal.apps.schedule.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import nl.klrnbk.daan.appiecal.apps.schedule.api.facade.ScheduleFacade
import nl.klrnbk.daan.appiecal.apps.schedule.api.models.schedule.ScheduleShift
import nl.klrnbk.daan.appiecal.apps.schedule.constants.DATE_TIME_ZONE_FORMAT
import nl.klrnbk.daan.appiecal.packages.common.responses.error.BaseErrorResponses
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.schedule.ScheduleResponse
import nl.klrnbk.daan.appiecal.packages.security.idp.models.JwtAuthenticationToken
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime

@RestController
@SecurityRequirement(name = "api-key")
@RequestMapping("/v1/schedule")
@BaseErrorResponses
class ScheduleController(
    private val scheduleFacade: ScheduleFacade,
) {
    @GetMapping("/me")
    @Operation(summary = "Returns the synced schedule")
    @ApiResponse(
        responseCode = "200",
        description = "Returns the list of shifts (formatted with activities) of a user",
        content = [
            Content(
                mediaType = "application/json",
                schema = Schema(implementation = ScheduleResponse::class),
                examples = [ExampleObject(value = ScheduleResponse.SCHEDULE_RESPONSE_EXAMPLE)],
            ),
        ],
    )
    @PreAuthorize("@scopes.hasScope(authentication, 'https://klrnbk.nl/projects/appiecal:use')")
    fun getSchedule(
        authentication: JwtAuthenticationToken,
        @Parameter(
            name = "start-date",
            description = "The start date",
            schema =
                Schema(
                    type = "string",
                    format = DATE_TIME_ZONE_FORMAT,
                    example = "2025-04-28T00:00:00+02:00",
                ),
        )
        @RequestParam("start-date", required = true)
        startDate: ZonedDateTime,
        @Parameter(
            name = "end-date",
            description = "The end date",
            schema =
                Schema(
                    type = "string",
                    format = DATE_TIME_ZONE_FORMAT,
                    example = "2025-04-28T00:00:00+02:00",
                ),
        )
        @RequestParam("end-date", required = true)
        endDate: ZonedDateTime,
    ): ScheduleResponse<ScheduleShift> = scheduleFacade.getScheduleFromId(authentication.principal, startDate, endDate)

    @GetMapping("/{userId}")
    @Operation(summary = "Returns the synced schedule (SERVICE ACCOUNT ONLY)")
    @ApiResponse(
        responseCode = "200",
        description = "Returns the list of shifts (formatted with activities) of a user",
        content = [
            Content(
                mediaType = "application/json",
                schema = Schema(implementation = ScheduleResponse::class),
                examples = [ExampleObject(value = ScheduleResponse.SCHEDULE_RESPONSE_EXAMPLE)],
            ),
        ],
    )
    @PreAuthorize("@scopes.hasScope(authentication, 'https://klrnbk.nl/projects/appiecal:use') && @scopes.isServiceAccount(authentication)")
    fun getScheduleFromId(
        @Parameter(
            name = "userId",
            description = "The id of the user registered at IDP",
            schema = Schema(type = "string"),
        )
        @PathVariable userId: String,
        @Parameter(
            name = "start-date",
            description = "The start date",
            schema =
                Schema(
                    type = "string",
                    format = DATE_TIME_ZONE_FORMAT,
                    example = "2025-04-28T00:00:00+02:00",
                ),
        )
        @RequestParam("start-date", required = true)
        startDate: ZonedDateTime,
        @Parameter(
            name = "end-date",
            description = "The end date",
            schema =
                Schema(
                    type = "string",
                    format = DATE_TIME_ZONE_FORMAT,
                    example = "2025-04-28T00:00:00+02:00",
                ),
        )
        @RequestParam("end-date", required = true)
        endDate: ZonedDateTime,
    ): ScheduleResponse<ScheduleShift> = scheduleFacade.getScheduleFromId(userId, startDate, endDate)
}
