package ru.aleshi.scoreboards.core

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
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
    fun loadFromResources(path: String): Completable =
        Completable.fromAction {
            props.clear()
            props.load(InputStreamReader(javaClass.getResourceAsStream(path), "UTF-8"))
        }
            .subscribeOn(Schedulers.io())

    /**
     * Returns a string associated with the [key]
     */
    fun getString(key: String): String =
        props.getProperty(key)

}