package dev.ijskoud.appiecal.store

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.charset.StandardCharsets
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Makes it possible to store data in a JSON file
 * While also being able to read the data instantly
 */
open class Store<Schema>(data: Schema?, private val path: String) {
    private var _data: Schema

    init {
        _data = data ?: read()
    }

    /**
     * Get the stored data
     */
    fun get(): Schema {
        return _data
    }

    /**
     * Update the storage with the provided data
     * @param data The data to update the storage with
     */
    fun update(data: Schema) {
        _data = data
        save()
    }

    /**
     * Save the data to the storage file
     */
    fun save() {
        val jsonData = toJson(_data)
        File(path).writeText(jsonData, StandardCharsets.UTF_8)
    }

    /**
     * Read the data from the storage file
     */
    private fun read(): Schema {
        val data = Files.readString(Paths.get(path), StandardCharsets.UTF_8)
        return fromJson(data)
    }

    /**
     * Convert the object to JSON string (You can use Gson or other libraries)
     */
    private fun toJson(data: Schema): String {
        // Use a library like Gson to convert the object to a JSON string
        return Gson().toJson(data)
    }

    /**
     * Parse the data from the JSON string
     * You can use Gson or any other JSON parsing library
     */
    protected open fun fromJson(data: String): Schema {
        return Gson().fromJson(data, (object : TypeToken<Schema>() {}.type))
    }

    /**
     * Parse the data field key and value (Override this for custom parsing)
     * @param key The field key
     * @param value The value to parse
     * @returns parsed value
     */
    protected fun parse(key: String, value: Any?): Any? {
        return if (key.contains("date", ignoreCase = true)) {
            value?.let { java.sql.Date.valueOf(it.toString()) }  // Example of parsing a date field
        } else value
    }
}
