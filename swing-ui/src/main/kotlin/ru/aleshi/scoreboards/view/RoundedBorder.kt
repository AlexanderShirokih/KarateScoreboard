package ru.aleshi.scoreboards.view

import java.awt.*
import java.awt.geom.RoundRectangle2D
import javax.swing.border.AbstractBorder

/**
 * Rounded border for component with specified [radius], [lineColor] and thickness.
 */
class RoundedBorder(
    private val lineColor: Color,
    private val radius: Int,
    thickness: Float
) : AbstractBorder() {

    private val borderStroke = BasicStroke(thickness)

    override fun paintBorder(c: Component, g: Graphics, x: Int, y: Int, width: Int, height: Int) {
        (g.create() as Graphics2D).apply {
            this.color = lineColor
            this.stroke = borderStroke
            draw(
                RoundRectangle2D.Float(
                    x.toFloat(),
                    y.toFloat(),
                    (width - 1).toFloat(),
                    (height - 1).toFloat(),
                    radius.toFloat(),
                    radius.toFloat()
                )
            )
            dispose()
        }
    }

    override fun getBorderInsets(c: Component): Insets {
        return super.getBorderInsets(c, Insets(radius, radius, radius, radius))
    }

    override fun getBorderInsets(c: Component, insets: Insets): Insets {
        return insets.apply {
            left = radius / 2
            top = radius / 2
            right = radius / 2
            bottom = radius / 2
        }
    }

    override fun isBorderOpaque(): Boolean = false
}