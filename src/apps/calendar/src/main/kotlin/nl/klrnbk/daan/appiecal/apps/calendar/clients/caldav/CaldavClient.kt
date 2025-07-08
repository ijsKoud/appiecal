package nl.klrnbk.daan.appiecal.apps.calendar.clients.caldav

import nl.klrnbk.daan.appiecal.apps.calendar.constants.GET_CALENDAR_HOME_SET
import nl.klrnbk.daan.appiecal.apps.calendar.constants.GET_CALENDAR_LIST
import nl.klrnbk.daan.appiecal.apps.calendar.constants.GET_PRINCIPAL_XML
import nl.klrnbk.daan.appiecal.apps.calendar.exceptions.InvalidDavCredentialsException
import nl.klrnbk.daan.appiecal.apps.calendar.helpers.extractHrefFromPropfindResponse
import nl.klrnbk.daan.appiecal.apps.calendar.helpers.extractVeventCalendars
import nl.klrnbk.daan.appiecal.apps.calendar.helpers.hrefToValidUrl
import nl.klrnbk.daan.appiecal.packages.common.exceptions.DownstreamServiceErrorException
import nl.klrnbk.daan.appiecal.packages.common.exceptions.MalformedResponseBodyException
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CaldavClient {
    private val httpClient = OkHttpClient()
    private val logger = LoggerFactory.getLogger(CaldavClient::class.java)

    fun getPrincipal(
        baseUrl: String,
        authScope: String,
        authToken: String,
    ): String {
        val requestBody = GET_PRINCIPAL_XML.toRequestBody("application/xml".toMediaType())
        val request =
            getRequestBuilder(baseUrl, authScope, authToken)
                .method("PROPFIND", requestBody)
                .build()

        val result = handleApiCall(httpClient.newCall(request))
        val href = extractHrefFromPropfindResponse(result)
        if (href.isNullOrEmpty()) throw MalformedResponseBodyException()

        return hrefToValidUrl(href, baseUrl)
    }

    fun getCalendarHomeSet(
        url: String,
        authScope: String,
        authToken: String,
    ): String {
        val requestBody = GET_CALENDAR_HOME_SET.toRequestBody("application/xml".toMediaType())
        val request =
            getRequestBuilder(url, authScope, authToken)
                .method("PROPFIND", requestBody)
                .build()

        val result = handleApiCall(httpClient.newCall(request))
        val href = extractHrefFromPropfindResponse(result)
        if (href.isNullOrEmpty()) throw MalformedResponseBodyException()

        return hrefToValidUrl(href, url)
    }

    fun getCalendarList(
        url: String,
        authScope: String,
        authToken: String,
    ): List<Pair<String, String>> {
        val requestBody = GET_CALENDAR_LIST.toRequestBody("application/xml".toMediaType())
        val request =
            getRequestBuilder(url, authScope, authToken)
                .method("PROPFIND", requestBody)
                .header("Depth", "1")
                .build()

        val result = handleApiCall(httpClient.newCall(request))
        return extractVeventCalendars(result)
    }

    private fun handleApiCall(call: Call): String {
        val response = call.execute()
        val errorStatus = response.code
        if (errorStatus < 500 && errorStatus >= 400) {
            logger.error("Downstream service call was not successful; Detail rediscovery may be required; status=$errorStatus;")
            throw InvalidDavCredentialsException()
        }

        if (!response.isSuccessful) {
            val errorBody = response.body
            if (errorBody != null) {
                val stringyfiedBody = errorBody.string()
                logger.error("Downstream service call was not successful; status=$errorStatus; error=$stringyfiedBody")
            }

            throw DownstreamServiceErrorException(errorStatus, response.message)
        }

        val body = response.body
        if (body == null) throw MalformedResponseBodyException()

        return body.string()
    }

    private fun getRequestBuilder(
        url: String,
        authScope: String,
        authToken: String,
    ): Request.Builder =
        Request
            .Builder()
            .url(url)
            .header("Authorization", "$authScope $authToken")
            .header("Content-Type", "application/xml")
            .build()
            .newBuilder()
}
