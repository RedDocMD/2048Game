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

fun generateAllSuccessors(board: Board, direction: Direction): MutableList<Pair<Double, Board>> {
    val copyBoard = board.copyOf()
    copyBoard.moveNoRandom(direction)
    val nextBoards = mutableListOf<Pair<Double, Board>>()
    for (i in 0 until board.rows) {
        for (j in 0 until board.columns) {
            if (copyBoard[i, j] == 0) {
                var nextBoard = copyBoard.copyOf()
                nextBoard[i, j] = 2
                nextBoards.add(Pair(0.9, nextBoard))
                nextBoard = copyBoard.copyOf()
                nextBoard[i, j] = 4
                nextBoards.add(Pair(0.1, nextBoard))
            }
        }
    }
    return nextBoards
}

fun depthLimitedSearch(board: Board, height: Int): Pair<Int, Direction> {
    if (height == 0) {
        return Pair(staticEvaluator(board), Direction.NONE)
    } else {
        var direction = Direction.NONE
        var maximum = 0
        enumValues<Direction>().forEach { dir ->
            if (dir != Direction.NONE) {
                var sum = 0.0
                val successors = generateAllSuccessors(board, dir)
                successors.forEach {
                    val (probability, successor) = it
                    sum += probability * depthLimitedSearch(successor, height - 1).first
                }
                val average = (sum / successors.size).toInt()
                if (average > maximum) {
                    maximum = average
                    direction = dir
                }
            }
        }
        return Pair(maximum, direction)
    }
}

fun getNextAIMove(board: Board): Direction {
    val heightLimit = 2
    return depthLimitedSearch(board, heightLimit).second
}