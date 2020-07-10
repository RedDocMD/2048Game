fun staticEvaluator(board: Board): Int {
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

fun generateAllSuccessors(board: Board, direction: Direction): MutableList<Board> {
    val copyBoard = board.copyOf()
    copyBoard.moveNoRandom(direction)
    val nextBoards = mutableListOf<Board>()
    for (i in 0 until board.rows) {
        for (j in 0 until board.columns) {
            if (copyBoard[i, j] == 0) {
                var nextBoard = copyBoard.copyOf()
                nextBoard[i, j] = 2
                nextBoards.add(nextBoard)
                nextBoard = copyBoard.copyOf()
                nextBoard[i, j] = 4
                nextBoards.add(nextBoard)
            }
        }
    }
    return nextBoards
}

fun generateAllSuccessors(board: Board): List<Board> {
    val nextBoards = generateAllSuccessors(board, Direction.UP)
    nextBoards.addAll(generateAllSuccessors(board, Direction.DOWN))
    nextBoards.addAll(generateAllSuccessors(board, Direction.LEFT))
    nextBoards.addAll(generateAllSuccessors(board, Direction.RIGHT))
    return nextBoards
}