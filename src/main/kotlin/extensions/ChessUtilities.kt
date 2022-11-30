package extensions

import domain.CellType.EMPTY
import domain.CellType.QUEEN
import domain.ChessBoard
import java.util.Collections
import kotlin.math.abs

const val RECURSIVE_ALGORITHM_LIMIT = 13

object ChessUtilities {
    var possibleVariants = mutableMapOf<Int, ChessBoard>()
    var placedQueensCords = mutableListOf<Pair<Int, Int>>()

    fun placeNQueens(chess: ChessBoard, column: Int = 0) {
        possibleVariants.clear()
        if (chess.boardSize <= RECURSIVE_ALGORITHM_LIMIT)
            queensRecursive(chess, column)
        else
            queensHeuristic(chess)
    }

    private fun queensHeuristic(chess: ChessBoard) {
        val additionalList = mutableListOf<Int>()

        with (chess.boardSize % 12) {
            additionalList.addAll(2..chess.boardSize step 2)
            if (this == 3 || this == 9)
                moveElementToEndOfCollection(additionalList, 2)

            additionalList.addAll(
                (1..chess.boardSize step 2).toList().apply {
                    if (this@with == 8) {
                        for (i in 0 .. lastIndex step 2)
                            Collections.swap(this, i, i + 1)
                    }
                }
            )

            if (this == 2 && chess.boardSize >= 3) {
                with (additionalList) {
                    Collections.swap(
                        this,
                        indexOf(1),
                        indexOf(3)
                    )
                }
                if (chess.boardSize >= 5)
                    moveElementToEndOfCollection(additionalList, 5)
            }

            if (this == 3 || this == 9) {
                moveElementToEndOfCollection(additionalList, 1)
                moveElementToEndOfCollection(additionalList, 3)
            }
        }

        possibleVariants[possibleVariants.size] = ChessBoard(chess.boardSize).apply {
            board = Array(chess.boardSize) { CharArray(chess.boardSize) { EMPTY.name[0] } }
            for (i in 0 until boardSize) {
                board[additionalList[i] - 1][i] = QUEEN.name[0]
            }
        }
    }

    private fun <T> moveElementToEndOfCollection(additionalList: MutableList<T>, element: T) {
        additionalList.remove(element)
        additionalList.add(element)
    }

    private fun queensRecursive(
        chess: ChessBoard,
        column: Int = 0
    ) {
        if (chess.boardSize < 4)
            error("The problem has no solutions for n < 4")

        if (column == chess.boardSize) {
            with (possibleVariants) {
                put(size, ChessBoard(chess))
            }
            return
        }

        (0 until chess.boardSize).forEach { i ->
            if (Pair(i, column).isSafeForQueen()) {
                chess.board[i][column] = QUEEN.name[0]
                placedQueensCords.add(Pair(i, column))

                queensRecursive(chess, column + 1)

                placedQueensCords.remove(Pair(i, column))
                chess.board[i][column] = EMPTY.name[0]
            }
        }
    }


    private fun Pair<Int,Int>.isSafeForQueen() : Boolean =
        placedQueensCords.none {
            it.first == this.first || it.second == this.second ||
                    abs(it.first - this.first) == abs(it.second - this.second)
        }
}