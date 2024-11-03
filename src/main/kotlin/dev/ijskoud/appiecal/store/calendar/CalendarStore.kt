package dev.ijskoud.appiecal.store.calendar

import com.google.gson.Gson
import dev.ijskoud.appiecal.ah.rooster.Shift
import dev.ijskoud.appiecal.store.Store
import java.nio.file.Paths

class CalendarStore : Store<Array<Shift>>(
    data = null,
    path = Paths.get(System.getProperty("user.dir"), "data", "calendar.json").toString()
) {
    companion object {
        private var instance: CalendarStore? = null

        fun getInstance(): CalendarStore {
            if (instance == null) {
                instance = CalendarStore()
            }

            return instance!!
        }
    }

    override fun fromJson(data: String): Array<Shift> {
        return Gson().fromJson(data, Array<Shift>::class.java)
    }
}
