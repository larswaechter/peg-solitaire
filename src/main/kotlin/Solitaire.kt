class Solitaire(
    // -1 = Non-Field; 0 = Empty; 1 = Pin
    private val board: Array<IntArray> = arrayOf(
        intArrayOf(-1, -1, 1, 1, 1, -1, -1),
        intArrayOf(-1, -1, 1, 1, 1, -1, -1),
        intArrayOf(1, 1, 1, 1, 1, 1, 1),
        intArrayOf(1, 1, 1, 0, 1, 1, 1),
        intArrayOf(1, 1, 1, 1, 1, 1, 1),
        intArrayOf(-1, -1, 1, 1, 1, -1, -1),
        intArrayOf(-1, -1, 1, 1, 1, -1, -1),
    ),
    private val remainingPegs: Int = 32
) {
    companion object {
        val memory = mutableMapOf<Int, Pair<Move?, Boolean>>()
    }

    fun move(move: Move): Solitaire {
        val newBoard = board.map { it.clone() }.toTypedArray()

        val toRemove = move.getPegToRemove()
        newBoard[move.from.first][move.from.second] = 0
        newBoard[toRemove.first][toRemove.second] = 0
        newBoard[move.to.first][move.to.second] = 1

        return Solitaire(newBoard, remainingPegs - 1)
    }

    fun hasWon(): Boolean = this.remainingPegs == 1 && board[3][3] == 1

    private fun getPossibleMoves(): List<Move> {
        val moves = mutableListOf<Move>()

        for ((rowIdx, row) in board.withIndex()) {
            for ((cellIdx, cell) in row.withIndex()) {
                if (cell != 1) continue

                val from = Pair(rowIdx, cellIdx)

                // Left => Right
                if (cellIdx <= 4 && row[cellIdx + 1] == 1 && row[cellIdx + 2] == 0)
                    moves.add(Move(from, Pair(rowIdx, cellIdx + 2)))

                // Right => Left
                if (cellIdx >= 2 && row[cellIdx - 1] == 1 && row[cellIdx - 2] == 0)
                    moves.add(Move(from, Pair(rowIdx, cellIdx - 2)))

                // Up => Down
                if (rowIdx <= 4 && board[rowIdx + 1][cellIdx] == 1 && board[rowIdx + 2][cellIdx] == 0)
                    moves.add(Move(from, Pair(rowIdx + 2, cellIdx)))

                // Down => Up
                if (rowIdx >= 2 && board[rowIdx - 1][cellIdx] == 1 && board[rowIdx - 2][cellIdx] == 0)
                    moves.add(Move(from, Pair(rowIdx - 2, cellIdx)))
            }
        }

        return moves
    }

    fun dfs(
        game: Solitaire = this,
    ): Pair<Move?, Boolean> {
        if (game.hasWon()) return Pair(null, true)

        // Read from memory
        val hash = game.board.contentDeepHashCode()
        if (memory.containsKey(hash))
            return memory[hash]!!

        val moves = game.getPossibleMoves()
        if (moves.isEmpty()) return Pair(null, false)

        // Play each move and call recursively. Pick first solution that was found.
        for (move in moves) {
            val newGame = game.move(move)
            val hasWon = dfs(newGame).second

            // Return move that lead to win & store in memory
            if (hasWon) {
                memory[hash] = Pair(move, true)
                return memory[hash]!!
            }
        }

        // Case when no solution was found.
        return Pair(moves[0], false)
    }

    override fun toString(): String {
        var str = " " + "-".repeat(15) + "\n"
        for (row in board) {
            str += "| "
            for (item in row)
                str += when (item) {
                    0 -> "o "
                    1 -> "X "
                    else -> "  "
                }
            str += "|\n"
        }

        str += " " + "-".repeat(15)

        return str
    }
}