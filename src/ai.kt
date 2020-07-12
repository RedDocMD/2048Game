fun staticEvaluator(board: Board): Int {
    // More points = better board
    val state = board.gameState
    if (state == GameState.WON) return 1000000
    else if (state == GameState.LOST) return 0
    var utility = board.points

    var flattenedBoard = board.flattenedBoard()
    flattenedBoard = flattenedBoard.sortedDescending()
    flattenedBoard = flattenedBoard.distinct()

    val neighborBump = board.points
    val maxValue = flattenedBoard[0]

    // Adding utility for adjacent tiles of equal value
    for (i in 0 until board.rows) {
        for (j in 0 until board.columns) {
            val neighbors = board.neighbors(i, j)
            for (neighbor in neighbors) {
                if (neighbor == board[i, j]) {
                    utility += ((neighbor.toDouble() / maxValue.toDouble()) * neighborBump).toInt()
                }
            }
        }
    }

    // Extra value for largest value in bottom-left corner
    val cornerBump = board.points

    if (board[board.rows - 1, board.columns - 1] == flattenedBoard[0]) {
        utility += cornerBump
//        var count = 0
//        if (flattenedBoard.size > 1 && board[board.rows - 1, board.columns - 2] == flattenedBoard[1]) {
//            utility += (cornerBump * (flattenedBoard[1].toDouble() / flattenedBoard[0].toDouble())).toInt()
//            count++
//        }
//        if (flattenedBoard.size > 1 && board[board.rows - 2, board.columns - 1] == flattenedBoard[1]) {
//            utility += (cornerBump * (flattenedBoard[1].toDouble() / flattenedBoard[0].toDouble())).toInt()
//            count++
//        }
//        if (count == 1) {
//            if (flattenedBoard.size > 2 && (board[board.rows - 2, board.columns - 1] == flattenedBoard[2] || board[board.rows - 1, board.columns - 2] == flattenedBoard[2])) {
//                utility += (cornerBump * (flattenedBoard[2].toDouble() / flattenedBoard[0].toDouble())).toInt()
//            }
//        }
    }
    return utility
}

fun depthLimitedSearch(board: Board, height: Int): Pair<Int, Direction> {
    if (height == 0) {
        return Pair(staticEvaluator(board), Direction.NONE)
    } else {
        var direction = Direction.NONE
        var maximum = 0
        enumValues<Direction>().forEach { dir ->
            if (dir != Direction.NONE) {
                val copyBoard = board.copyOf()
                copyBoard.moveNoRandom(dir)
                if (copyBoard != board) {
                    var count = 0
                    var sum = 0.0
                    val average: Int
                    if (copyBoard.gameState != GameState.RUNNING) {
                        average = staticEvaluator(copyBoard)
                    } else {
                        for (i in 0 until copyBoard.rows) {
                            for (j in 0 until copyBoard.columns) {
                                if (copyBoard[i, j] == 0) {
                                    copyBoard[i, j] = 2
                                    sum += depthLimitedSearch(copyBoard, height - 1).first * 0.9
                                    copyBoard[i, j] = 4
                                    sum += depthLimitedSearch(copyBoard, height - 1).first * 0.1
                                    copyBoard[i, j] = 0
                                    count += 2
                                }
                            }
                        }
                        average = (sum / count).toInt()
                    }
                    if (average >= maximum) {
                        maximum = average
                        direction = dir
                    }
                }
            }
        }
        return Pair(maximum, direction)
    }
}

fun getNextAIMove(board: Board): Direction {
    val heightLimit = 3
    return depthLimitedSearch(board, heightLimit).second
}