package ru.aleshi.scoreboards.core

import kotlinx.coroutines.coroutineScope
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
        props.clear()
        props.load(InputStreamReader(javaClass.getResourceAsStream(path), "UTF-8"))
    }

    /**
     * Returns a string associated with the [key]
     */
    fun getString(key: String): String =
        props.getProperty(key)

}