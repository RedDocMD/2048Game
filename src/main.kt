import org.deep.game.BoardComponent
import org.deep.game.Direction
import org.deep.game.GameState
import org.deep.game.getNextAIMove
import java.awt.BorderLayout
import java.awt.EventQueue
import java.awt.FlowLayout
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
    topPanel.layout = FlowLayout(FlowLayout.CENTER, 30, 5)
    val pointsLabel = JLabel("Points: ${board.points}")
    val movesLabel = JLabel("Moves: 0")
    topPanel.add(pointsLabel)
    topPanel.add(movesLabel)

    frame.add(topPanel, BorderLayout.NORTH)

    val buttonPanel = JPanel()

    val aiMoveButton = JButton("AI Move")
    aiMoveButton.addActionListener {
        val direction = getNextAIMove(board.board)
        board.move(direction)
        pointsLabel.text = "Points: ${board.points}"
    }

    val resetButton = JButton("Reset")
    resetButton.addActionListener {
        board.reset()
        pointsLabel.text = "Points: ${board.points}"
        movesLabel.text = "Moves: 0"
    }

    val aiStartButton = JButton("AI Start")
    aiStartButton.addActionListener {
        aiMoveButton.isEnabled = false
        aiStartButton.isEnabled = false
        resetButton.isEnabled = false
        Thread {
            var moveCount = 0
            while (board.board.gameState == GameState.RUNNING) {
                val direction = getNextAIMove(board.board)
                if (direction == Direction.NONE) break
                moveCount += 1
                EventQueue.invokeLater {
                    board.move(direction)
                    pointsLabel.text = "Points: ${board.points}"
                    movesLabel.text = "Moves: $moveCount"
                }
                Thread.sleep(100)
            }
            aiMoveButton.isEnabled = true
            aiStartButton.isEnabled = true
            resetButton.isEnabled = true
        }.start()
    }

    buttonPanel.add(aiMoveButton)
    buttonPanel.add(aiStartButton)
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