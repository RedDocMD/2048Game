import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.geom.RoundRectangle2D

const val roundedPercentage = 0.2

data class Tile(val tileWidth: Double, val tileHeight: Double, val tileColor: Color, val tileContent: String) {
    fun display(x: Double, y: Double, graphics2D: Graphics2D) {
        val rect = RoundRectangle2D.Double(x, y, tileWidth, tileHeight, roundedPercentage * tileWidth, roundedPercentage * tileHeight)
        graphics2D.color = tileColor
        graphics2D.fill(rect)
        graphics2D.color = Color.BLACK
        val tileFont = Font("Sans Serif", Font.BOLD, 22)
        graphics2D.font = tileFont
        val metrics = graphics2D.getFontMetrics(tileFont)
        val textHeight = metrics.height
        val textWidth = metrics.stringWidth(tileContent)
        graphics2D.drawString(tileContent, (x + tileWidth / 2 - textWidth / 2).toInt(), (y + tileHeight / 2 + textHeight / 2).toInt())
    }
}