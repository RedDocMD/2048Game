import java.awt.EventQueue
import javax.swing.JFrame

fun main() {
    val frame = JFrame()
    val board = Board(4, 4)
    frame.add(board)
    frame.pack()
    frame.title = "2048 Game"
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    EventQueue.invokeLater { frame.isVisible = true }
}