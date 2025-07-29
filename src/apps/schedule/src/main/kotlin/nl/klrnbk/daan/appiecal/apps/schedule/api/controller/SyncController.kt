package nl.klrnbk.daan.appiecal.apps.schedule.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import nl.klrnbk.daan.appiecal.apps.schedule.api.facade.SyncFacade
import nl.klrnbk.daan.appiecal.apps.schedule.constants.DATE_TIME_ZONE_FORMAT
import nl.klrnbk.daan.appiecal.apps.schedule.exceptions.MissingAccessTokenException
import nl.klrnbk.daan.appiecal.packages.common.responses.error.BaseErrorResponses
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.syncing.SyncStatusResponse
import nl.klrnbk.daan.appiecal.packages.security.idp.models.JwtAuthenticationToken
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime

@RestController
@SecurityRequirement(name = "api-key")
@RequestMapping("/v1/sync")
@BaseErrorResponses
class SyncController(
    private val syncFacade: SyncFacade,
) {
    @PostMapping("/{userId}")
    @Operation(summary = "Syncs the schedule with our own records (SERVICE ACCOUNT ONLY)")
    @ApiResponse(responseCode = "200", description = "Returns the sync results")
    @ApiResponse(
        responseCode = "409",
        description = "Missing or invalid Azure Token",
        content = [
            Content(
                mediaType = "application/problem+json",
                schema = Schema(implementation = MissingAccessTokenException::class),
                examples = [ExampleObject(value = MissingAccessTokenException.EXAMPLE)],
            ),
        ],
    )
    @PreAuthorize("@scopes.hasScope(authentication, 'https://klrnbk.nl/projects/appiecal:use') && @scopes.isServiceAccount(authentication)")
    fun syncSchedule(
        @Parameter(
            name = "userId",
            description = "The id of the user registered at IDP",
            schema = Schema(type = "string"),
        )
        @PathVariable
        userId: String,
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
        authorization: JwtAuthenticationToken,
    ): SyncStatusResponse = syncFacade.syncSchedule(authorization, userId, startDate, endDate)
}
