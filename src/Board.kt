import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JOptionPane
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

enum class Direction {
    LEFT, RIGHT, UP, DOWN
}

enum class GameState {
    RUNNING, WON, LOST
}

data class Board(val rows: Int, val columns: Int) : JPanel() {
    private val tileWidth = 75.0
    private val tileHeight = 75.0
    private val verticalSpace = 8.0
    private val horizontalSpace = 8.0
    private val tiles: Array<Array<Tile>>
    private val board: Array<Array<Int>>
    private val totalWidth = columns * tileWidth + (columns + 1) * horizontalSpace
    private val totalHeight = rows * tileHeight + (rows + 1) * verticalSpace

    val gameState: GameState
        get() {
            val flattenedBoard = board.flatten()
            return when {
                2048 in flattenedBoard -> GameState.WON
                0 !in flattenedBoard -> GameState.LOST
                else -> GameState.RUNNING
            }
        }

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

    private operator fun set(i: Int, j: Int, number: Int) {
        board[i][j] = number
        tiles[i][j].tileColor = colors[number]
        tiles[i][j].tileContent = if (number == 0) "" else number.toString()
        repaint()
    }

    operator fun get(i: Int, j: Int): Int {
        return board[i][j]
    }

    fun move(direction: Direction) {
        when (gameState) {
            GameState.LOST -> JOptionPane.showMessageDialog(this,
                    "Game has been lost!",
                    "Game over",
                    JOptionPane.INFORMATION_MESSAGE)
            GameState.WON -> JOptionPane.showMessageDialog(this,
                    "Game has been won!",
                    "Game over",
                    JOptionPane.INFORMATION_MESSAGE)
            else -> {
                // Deep copy of board
                val newBoard = Array(rows) { i -> Array(columns) { j -> board[i][j] } }

                when (direction) {
                    Direction.LEFT -> {
                        // First move the tiles
                        for (i in 0 until rows) {
                            var left = 0
                            while (left < columns && newBoard[i][left] != 0) left++
                            for (j in left until columns) {
                                for (jj in j + 1 until columns) {
                                    if (newBoard[i][jj] != 0) {
                                        newBoard[i][j] = newBoard[i][jj]
                                        newBoard[i][jj] = 0
                                        break
                                    }
                                }
                            }
                        }
                        // Then combine
                        for (i in 0 until rows) {
                            for (j in 0 until columns - 1) {
                                if (newBoard[i][j] != 0 && newBoard[i][j] == newBoard[i][j + 1]) {
                                    newBoard[i][j] *= 2
                                    newBoard[i][j + 1] = 0
                                    for (jj in j + 2 until columns) {
                                        newBoard[i][jj - 1] = newBoard[i][jj]
                                        newBoard[i][jj] = 0
                                    }
                                }
                            }
                        }
                    }
                }

                // Choose a random square and randomly set it to 2 or 4
                val random = Random.Default
                val lim = rows * columns
                var pos = random.nextInt(lim)
                while (newBoard[pos / rows][pos % rows] != 0) pos = random.nextInt(lim)
                val num = if (random.nextInt(2) == 0) 2 else 4
                newBoard[pos / rows][pos % columns] = num

                // Now update the actual board
                for (i in 0 until rows) {
                    for (j in 0 until columns) {
                        val number = newBoard[i][j]
                        board[i][j] = number
                        tiles[i][j].tileColor = colors[number]
                        tiles[i][j].tileContent = if (number == 0) "" else number.toString()
                    }
                }
                repaint()

                when (gameState) {
                    GameState.LOST -> JOptionPane.showMessageDialog(this,
                            "Game has been lost!",
                            "No more moves",
                            JOptionPane.INFORMATION_MESSAGE)
                    GameState.WON -> JOptionPane.showMessageDialog(this,
                            "Game has been won!",
                            "No more moves",
                            JOptionPane.INFORMATION_MESSAGE)
                    else -> {}
                }
            }
        }
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