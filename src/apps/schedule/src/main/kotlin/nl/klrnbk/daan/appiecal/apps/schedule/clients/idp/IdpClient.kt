package nl.klrnbk.daan.appiecal.apps.schedule.clients.idp

import nl.klrnbk.daan.appiecal.packages.common.retrofit.RetrofitClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class IdpClient(
    @Value("\${appiecal.schedule.idp.url}") baseUrl: String,
) : RetrofitClient() {
    private val apiClient = getRetrofitClient(IdpApiInterface::class.java, baseUrl)

    fun getAccessToken(
        token: String,
        userId: String,
    ): String {
        val call = apiClient.getAccessToken(token, userId)
        return handleApiCall(call)
    }
}
