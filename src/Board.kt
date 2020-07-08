import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel
import kotlin.random.Random

val colors = mapOf(
        0 to Color(206, 206, 206),
        2 to Color(232, 227, 237),
        4 to Color(227, 225, 206),
        8 to Color(240, 184, 116),
        16 to Color(237, 156, 115),
        32 to Color(237, 125, 93),
        64 to Color(242, 77, 51),
        128 to Color(247, 221, 72),
        256 to Color(255, 221, 0),
        512 to Color(180, 247, 35),
        1024 to Color(17, 245, 188),
        2048 to Color(17, 74, 245)
)

data class Board(val rows: Int, val columns: Int) : JPanel() {
    private val tileWidth = 75.0
    private val tileHeight = 75.0
    private val verticalSpace = 8.0
    private val horizontalSpace = 8.0
    private val tiles: Array<Array<Tile>>
    private val board: Array<Array<Int>>
    private val totalWidth = columns * tileWidth + (columns + 1) * horizontalSpace
    private val totalHeight = rows * tileHeight + (rows + 1) * verticalSpace

    init {
        tiles = Array(rows) { Array(columns) { Tile(tileWidth, tileHeight, colors[0], "") } }
        board = Array(rows) { Array(columns) { 0 } }
        preferredSize = Dimension(totalWidth.toInt(), totalHeight.toInt())

        // Choose two random squares to set as 2
        val random = Random.Default
        val lim = rows * columns
        val pos1 = random.nextInt(lim)
        var pos2 = random.nextInt(lim)
        while (pos1 == pos2) pos2 = random.nextInt(lim)
        this[pos1 / rows, pos1 % rows] = 2
        this[pos2 / rows, pos2 % rows] = 2
    }

    operator fun set(i: Int, j: Int, number: Int) {
        board[i][j] = number
        tiles[i][j].tileColor = colors[number]
        tiles[i][j].tileContent = if (number == 0) "" else number.toString()
        repaint()
    }

    operator fun get(i: Int, j: Int): Int {
        return board[i][j]
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        if (g != null) {
            val g2d = g as Graphics2D
            var y = verticalSpace
            for (row in tiles) {
                var x = horizontalSpace
                for (tile in row) {
                    tile.display(x, y, g2d)
                    x += tileWidth + horizontalSpace
                }
                y += tileHeight + verticalSpace
            }
        }
    }
}