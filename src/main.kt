import java.awt.BorderLayout
import java.awt.EventQueue
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel

fun main() {
    val frame = JFrame()

    val board = BoardComponent(4, 4)
    frame.add(board, BorderLayout.CENTER)

    val topPanel = JPanel()
    val pointsLabel = JLabel("Points: ${board.points}")
    topPanel.add(pointsLabel)
    frame.add(topPanel, BorderLayout.NORTH)

    val buttonPanel = JPanel()

    val aiMoveButton = JButton("AI Move")
    aiMoveButton.addActionListener {
        val direction = getNextAIMove(board.board)
        board.move(direction)
        pointsLabel.text = "Points: ${board.points}"
    }
    buttonPanel.add(aiMoveButton)

    val resetButton = JButton("Reset")
    resetButton.addActionListener {
        board.reset()
        pointsLabel.text = "Points: ${board.points}"
    }
    buttonPanel.add(resetButton)

    frame.add(buttonPanel, BorderLayout.SOUTH)

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

    // Start the game
    EventQueue.invokeLater { frame.isVisible = true }
}