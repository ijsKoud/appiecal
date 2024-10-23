package dev.ijskoud.appiecal.rooster

import dev.ijskoud.appiecal.authentication.AuthenticationService
import dev.ijskoud.appiecal.rooster.interfaces.AlbertHeijnVariables
import dev.ijskoud.appiecal.rooster.interfaces.ApiService
import dev.ijskoud.appiecal.rooster.interfaces.RoosterRequest
import dev.ijskoud.appiecal.rooster.interfaces.RoosterResponse
import dev.ijskoud.appiecal.store.auth.AuthStore
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class Repository(
    private val authenticator: AuthenticationService = AuthenticationService.getInstance(),
    private val store: AuthStore = AuthStore.getInstance(),
    private val logger: Logger = LoggerFactory.getLogger(Repository::class.java)
) {
    /**
     * Retrieves the access token from the store
     */
    private fun getAccessToken(): String {
        val data = store.get()

        if (!data.isExpired) {
            logger.debug("Access token is still valid")
            return data.accessToken
        }

        val newToken = authenticator.getNewToken(data.refreshToken)
        data.accessToken = newToken.accessToken
        data.refreshToken = newToken.refreshToken
        data.expireDate = newToken.expireDate

        store.save()
        logger.info("New access token acquired, valid until ${newToken.expireDate}")

        return data.accessToken
    }

    /**
     * Fetches the rooster
     * @param startDate The date the data should begin
     */
    suspend fun getRooster(startDate: Date? = null): RoosterResponse? {
        return try {
            val accessToken = getAccessToken()
            val dates = getDates(startDate)

            val retrofit = Retrofit.Builder()
                .baseUrl(ALBERTHEIJN_GRAPHQL_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(ApiService::class.java)
            val request = RoosterRequest(
                operationName = "scheduleByWeek",
                variables = AlbertHeijnVariables(dates.start, dates.end),
                query = """
                    query scheduleByWeek(${"$"}startDate: String!, ${"$"}endDate: String!) {
                        scheduleByWeek(startDate: ${"$"}startDate, endDate: ${"$"}endDate) {
                            startTime
                            endTime
                            minutes
                            storeId
                            leaveMinutes
                            store {
                                abbreviatedDisplayName
                                location
                                id
                                __typename
                            }
                            paidMinutes
                            sickMinutes
                            teamNames
                            __typename
                        }
                    }
                """.trimIndent()
            )

            val response = service.getRooster("Bearer $accessToken", request)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()
                println(errorBody?.string())
                return null
            }

            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    private fun getDates(startDate: Date?): DateRange {
        val (start, end) = getDateRange(startDate)

        return DateRange(
            start = "${start.toInstant().toString().split("T")[0]}T00:00:00Z",
            end = "${end.toInstant().toString().split("T")[0]}T00:00:00Z"
        )
    }

    companion object {
        private const val ALBERTHEIJN_GRAPHQL_URL = "https://api-gateway.ah.nl/external/ah/rtp/dex/graphql/"
    }
}

data class DateRange(val start: String, val end: String)