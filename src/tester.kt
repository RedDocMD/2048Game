import org.deep.game.Board
import org.deep.game.Direction
import org.deep.game.GameState
import org.deep.game.staticEvaluator

fun main() {
    val board = Board(4, 4, 50.0, 50.0)
    val steps = 1000
    for (i in 0 until steps) {
        println("Step " + i + "  Heuristic: " + staticEvaluator(board))
        val direction = enumValues<Direction>().toList().shuffled()[0]
        board.move(direction)
        if (board.gameState == GameState.LOST) {
            break
        }
    }
}