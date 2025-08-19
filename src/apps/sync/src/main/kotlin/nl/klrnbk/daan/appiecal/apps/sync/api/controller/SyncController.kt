package nl.klrnbk.daan.appiecal.apps.sync.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import nl.klrnbk.daan.appiecal.apps.sync.api.facade.SyncFacade
import nl.klrnbk.daan.appiecal.apps.sync.clients.schedule.ScheduleClient
import nl.klrnbk.daan.appiecal.packages.common.responses.error.BaseErrorResponses
import nl.klrnbk.daan.appiecal.packages.common.shared.services.schedule.models.syncing.SyncStatusResponse
import nl.klrnbk.daan.appiecal.packages.security.idp.client.openid.OpenIdClient
import nl.klrnbk.daan.appiecal.packages.security.idp.models.JwtAuthenticationToken
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalTime
import java.time.ZonedDateTime

@RestController
@SecurityRequirement(name = "api-key")
@RequestMapping("/v1/sync")
@BaseErrorResponses
class SyncController(
    private val syncFacade: SyncFacade,
) {
    @GetMapping("/manual")
    @Operation(summary = "Sync the users calendar manually")
    @ApiResponse(responseCode = "200", description = "Returns the sync results")
    @PreAuthorize("@scopes.hasScope(authentication, 'https://klrnbk.nl/projects/appiecal:use')")
    fun manualSync(
        authentication: JwtAuthenticationToken,
        @Parameter(name = "start-date") @RequestParam("start-date", required = true) startDate: ZonedDateTime,
        @Parameter(name = "end-date") @RequestParam("end-date", required = true) endDate: ZonedDateTime,
    ): SyncStatusResponse = syncFacade.syncScheduleWithCalDav(authentication.principal, startDate, endDate)

    @GetMapping("/automatic/{userId}")
    @Operation(summary = "Sync the users calendar as SERVICE ACCOUNT")
    @ApiResponse(responseCode = "200", description = "Returns the sync results")
    @PreAuthorize("@scopes.hasScope(authentication, 'https://klrnbk.nl/projects/appiecal:use') && @scopes.isServiceAccount(authentication)")
    fun automaticSync(
        authentication: JwtAuthenticationToken,
        @PathVariable userId: String,
        @Parameter(name = "start-date") @RequestParam("start-date", required = true) startDate: ZonedDateTime,
        @Parameter(name = "end-date") @RequestParam("end-date", required = true) endDate: ZonedDateTime,
    ): SyncStatusResponse = syncFacade.syncScheduleWithCalDav(userId, startDate, endDate)
}
