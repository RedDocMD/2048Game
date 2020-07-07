import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel

data class Board(val rows: Int, val columns: Int) : JPanel() {
    private val tileWidth = 75.0
    private val tileHeight = 75.0
    private val verticalSpace = 8.0
    private val horizontalSpace = 8.0
    private val tiles: Array<Array<Tile>>
    private val totalWidth = columns * tileWidth + (columns + 1) * horizontalSpace
    private val totalHeight = rows * tileHeight + (rows + 1) * verticalSpace

    init {
        tiles = Array(rows) { Array(columns) { Tile(tileWidth, tileHeight, Color.RED, "1") } }
        preferredSize = Dimension(totalWidth.toInt(), totalHeight.toInt())
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        if (g != null) {
            val g2d = g as Graphics2D
            var y = verticalSpace
            for (i in 0 until rows) {
                var x = horizontalSpace
                for (j in 0 until columns) {
                    tiles[i][j].display(x, y, g2d)
                    x += tileWidth + horizontalSpace
                }
                y += tileHeight + verticalSpace
            }
        }
    }
}