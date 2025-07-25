package nl.klrnbk.daan.appiecal.apps.schedule.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import nl.klrnbk.daan.appiecal.apps.schedule.api.facade.RawRequestFacade
import nl.klrnbk.daan.appiecal.apps.schedule.api.models.schedule.ScheduleResponse
import nl.klrnbk.daan.appiecal.apps.schedule.clients.gql.models.schedule.GqlScheduleResponseSchedule
import nl.klrnbk.daan.appiecal.apps.schedule.constants.DATE_TIME_FORMAT
import nl.klrnbk.daan.appiecal.packages.common.responses.error.BaseErrorResponses
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/v1/raw-request")
@BaseErrorResponses
class RawRequestController(
    val rawRequestFacade: RawRequestFacade,
) {
    @PostMapping("/raw-schedule")
    @Operation(summary = "Fetch the schedule and get raw data from the @AH API")
    @ApiResponse(responseCode = "200", description = "Returns the list of shifts of a user")
    fun getRawSchedule(
        @Parameter(
            name = "access-token",
            description = "Azure Entra access token to access the @AH API's (can be retrieved via the 'AppieCal's idp-service')",
            schema = Schema(type = "string"),
        )
        @RequestParam(name = "access-token", required = true)
        token: String,
        @Parameter(
            name = "start-date",
            description = "The start date",
            schema =
                Schema(
                    type = "string",
                    format = DATE_TIME_FORMAT,
                    example = "2025-04-28T00:00:00",
                ),
        )
        @RequestParam("start-date", required = true)
        startDate: LocalDateTime,
        @Parameter(
            name = "end-date",
            description = "The end date",
            schema =
                Schema(
                    type = "string",
                    format = DATE_TIME_FORMAT,
                    example = "2025-04-28T00:00:00",
                ),
        )
        @RequestParam("end-date", required = true)
        endDate: LocalDateTime,
    ): List<GqlScheduleResponseSchedule> = rawRequestFacade.getRawSchedule(token, startDate, endDate)

    @PostMapping("/formatted-schedule")
    @Operation(summary = "Fetch the schedule and get formatted data (with activities) from the @AH API")
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
    fun getFormattedSchedule(
        @Parameter(
            name = "access-token",
            description = "Azure Entra access token to access the @AH API's (can be retrieved via the 'AppieCal's idp-service')",
            schema = Schema(type = "string"),
        )
        @RequestParam(name = "access-token", required = true)
        token: String,
        @Parameter(
            name = "start-date",
            description = "The start date",
            schema =
                Schema(
                    type = "string",
                    format = DATE_TIME_FORMAT,
                    example = "2025-04-28T00:00:00",
                ),
        )
        @RequestParam("start-date", required = true)
        startDate: LocalDateTime,
        @Parameter(
            name = "end-date",
            description = "The end date",
            schema =
                Schema(
                    type = "string",
                    format = DATE_TIME_FORMAT,
                    example = "2025-04-28T00:00:00",
                ),
        )
        @RequestParam("end-date", required = true)
        endDate: LocalDateTime,
    ): ScheduleResponse = rawRequestFacade.getFormattedSchedule(token, startDate, endDate)
}
