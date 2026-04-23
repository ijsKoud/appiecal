package nl.klrnbk.daan.appiecal.packages.common.retrofit

import com.fatboyindustrial.gsonjavatime.Converters
import com.google.gson.GsonBuilder
import nl.klrnbk.daan.appiecal.packages.common.constants.DATE_TIME_FORMAT
import nl.klrnbk.daan.appiecal.packages.common.exceptions.ApiException
import nl.klrnbk.daan.appiecal.packages.common.exceptions.DownstreamServiceErrorException
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

open class RetrofitClient {
    protected val logger: Logger = LoggerFactory.getLogger(javaClass)

    protected fun <T> handleApiCall(
        call: Call<T>,
        emptyResponse: Boolean = false,
    ): T {
        val response = call.execute()
        if (!response.isSuccessful) {
            val errorStatus = response.code()
            val errorBody = response.errorBody()
            if (errorBody != null) {
                val stringyfiedBody = errorBody.string()
                logger.error("Downstream service call was not successful; status=$errorStatus; error=$stringyfiedBody")
            }

            throw DownstreamServiceErrorException(errorStatus, response.message())
        }

        if (!emptyResponse) {
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

        return Unit as T
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

            val gson = Converters.registerZonedDateTime(GsonBuilder()).create()

            val retrofit =
                Retrofit
                    .Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build()

            return retrofit.create<T>(service)
        }
    }
}
