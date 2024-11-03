package dev.ijskoud.appiecal.calendar.methods

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase
import java.net.URI


class HttpReport(uri: String) : HttpEntityEnclosingRequestBase() {
    init {
        setURI(URI.create(uri))
    }

    override fun getMethod(): String {
        return METHOD_NAME
    }

    companion object {
        const val METHOD_NAME: String = "REPORT"
    }
}
