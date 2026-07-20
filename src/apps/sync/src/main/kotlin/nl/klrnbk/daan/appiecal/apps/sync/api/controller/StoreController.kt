package nl.klrnbk.daan.appiecal.apps.sync.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import nl.klrnbk.daan.appiecal.apps.sync.api.facade.StoreFacade
import nl.klrnbk.daan.appiecal.apps.sync.api.models.store.StoreInformation
import nl.klrnbk.daan.appiecal.packages.common.responses.error.BaseErrorResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/sync/store")
@BaseErrorResponses
class StoreController(
    private val storeFacade: StoreFacade,
) {
    @GetMapping("/get-store")
    @Operation(summary = "Get the store information")
    @ApiResponse(responseCode = "200", description = "Returns the store information")
    fun getStoreInformation(
        @Parameter(name = "store-id")
        @Parameter(
            name = "store-id",
            description = "The ID of the store",
            schema =
                Schema(
                    type = "string",
                    example = "1148",
                ),
        )
        storeId: String,
    ): StoreInformation = storeFacade.getStoreInformation(storeId)
}
