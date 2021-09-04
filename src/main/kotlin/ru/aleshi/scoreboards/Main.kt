package ru.aleshi.scoreboards

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import ru.aleshi.scoreboards.core.Resources

/**
 * Application entry point
 */
@ExperimentalCoroutinesApi
fun main() = runBlocking {
    Resources.loadFromResources("/i18n/ru-RU.prop")

    SwingMain.setup()
}

