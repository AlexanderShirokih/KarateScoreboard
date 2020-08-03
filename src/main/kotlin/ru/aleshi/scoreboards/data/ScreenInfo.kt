package ru.aleshi.scoreboards.data

import java.awt.Rectangle

/**
 * Describes information about screen device
 */
data class ScreenInfo(
    val index: Int,
    val bounds: Rectangle,
    val isFullscreen: Boolean,
    val isDefault: Boolean
)