package nl.klrnbk.daan.appiecal.apps.schedule.clients.gql

import nl.klrnbk.daan.appiecal.packages.common.retrofit.RetrofitClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class GqlClient(
    @Value("\${appiecal.schedule.gql.url}") baseUrl: String,
) : RetrofitClient() {
    private val apiClient = getRetrofitClient(GqlApiInterface::class.java, baseUrl)
}
