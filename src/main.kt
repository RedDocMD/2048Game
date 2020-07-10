import java.awt.BorderLayout
import java.awt.EventQueue
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel

fun main() {
    val frame = JFrame()
    val board = Board(4, 4)
    frame.add(board, BorderLayout.CENTER)
    val topPanel = JPanel()
    val pointsLabel = JLabel("Points: ${board.points}")
    topPanel.add(pointsLabel)
    frame.add(topPanel, BorderLayout.NORTH)
    frame.pack()
    frame.title = "2048 Game"
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.isResizable = false
    // Add the keyboard listener
    frame.addKeyListener(object : KeyAdapter() {
        override fun keyPressed(e: KeyEvent?) {
            if (e != null) {
                when (e.keyCode) {
                    KeyEvent.VK_LEFT -> board.move(Direction.LEFT)
                    KeyEvent.VK_RIGHT -> board.move(Direction.RIGHT)
                    KeyEvent.VK_UP -> board.move(Direction.UP)
                    KeyEvent.VK_DOWN -> board.move(Direction.DOWN)
                }
                pointsLabel.text = "Points: ${board.points}"
            }
        }
    })
    EventQueue.invokeLater { frame.isVisible = true }
}