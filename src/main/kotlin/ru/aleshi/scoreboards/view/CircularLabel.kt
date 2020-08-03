package ru.aleshi.scoreboards.view

import java.awt.Color
import java.awt.Graphics
import java.lang.Integer.min
import javax.swing.JComponent

/**
 * Colored circle JComponent
 */
class CircularLabel(private val color: Color) : JComponent() {

    var isActive: Boolean = false

    override fun paintComponent(g: Graphics) {
        if (!isActive) return

        val size = min(width, height)
        g.color = color
        g.fillOval(0, 0, size, size)
    }
}