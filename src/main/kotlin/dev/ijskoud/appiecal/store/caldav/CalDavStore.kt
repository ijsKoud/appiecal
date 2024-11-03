package dev.ijskoud.appiecal.store.caldav

import com.google.gson.Gson
import dev.ijskoud.appiecal.calendar.CalDavAuth
import dev.ijskoud.appiecal.store.Store
import java.nio.file.Paths

class CalDavStore : Store<CalDavAuth>(
    data = null,
    path = Paths.get(System.getProperty("user.dir"), "data", "caldav.json").toString()
) {
    companion object {
        private var instance: CalDavStore? = null

        fun getInstance(): CalDavStore {
            if (instance == null) {
                instance = CalDavStore()
            }

            return instance!!
        }
    }

    override fun fromJson(data: String): CalDavAuth {
        return Gson().fromJson(data, CalDavAuth::class.java)
    }
}
