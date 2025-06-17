package nl.klrnbk.daan.appiecal.packages.common.retrofit

import nl.klrnbk.daan.appiecal.packages.common.exceptions.ApiException
import nl.klrnbk.daan.appiecal.packages.common.exceptions.DownstreamServiceErrorException
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.http.HttpStatus
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class RetrofitClient {
    protected fun <T> handleApiCall(call: Call<T>): T {
        val response = call.execute()
        if (!response.isSuccessful) {
            val errorStatus = response.code()
            throw DownstreamServiceErrorException(errorStatus, "Downstream service error")
        }

        val body = response.body()
        if (body == null) {
            throw ApiException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Downstream service returned malformed response body",
                "Empty or invalid response body returned",
            )
        }

        return body
    }

    companion object {
        fun <T> getRetrofitClient(
            service: Class<T>,
            baseUrl: String,
        ): T {
            val loggingInterceptor =
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.NONE)

            val client =
                OkHttpClient
                    .Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()

            val retrofit =
                Retrofit
                    .Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()

            return retrofit.create<T>(service)
        }
    }
}
