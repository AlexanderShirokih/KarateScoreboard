package ru.aleshi.scoreboards.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.util.*

/**
 * Holds i18n resources.
 */
object Resources {

    private var props = Properties()

    /**
     * Loads property file with strings from resources [path]
     */
    suspend fun loadFromResources(path: String) = coroutineScope {
        withContext(Dispatchers.IO) {
            props.clear()
            props.load(InputStreamReader(javaClass.getResourceAsStream(path), "UTF-8"))
        }
    }

    /**
     * Returns a string associated with the [key]
     */
    fun getString(key: String): String =
        props.getProperty(key)

}