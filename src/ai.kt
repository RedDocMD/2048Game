fun static_evaluator(board: Board): Int {
    // More points = better board
    var utility = board.points

    // Adding utility for adjacent tiles of equal value
    for (i in 0 until board.rows) {
        for (j in 0 until board.columns) {
            val neighbors = board.neighbors(i, j)
            for (neighbor in neighbors) {
                if (neighbor == board[i, j]) {
                    utility += neighbor
                }
            }
        }
    }

    // Extra value for largest value in bottom-left corner
    val cornerBump = 100
    var flattenedBoard = board.flattenedBoard()
    flattenedBoard = flattenedBoard.sortedDescending()
    if (board[board.rows - 1, board.columns - 1] == flattenedBoard[0]) {
        utility += cornerBump
    }

    return utility
}