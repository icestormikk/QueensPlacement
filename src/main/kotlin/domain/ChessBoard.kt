package domain

import domain.CellType.EMPTY

data class ChessBoard(
    val boardSize: Int = 8,
) {
    val id: Long = (entityCounter++).toLong()
    var board: Array<CharArray> = Array(boardSize) { CharArray(boardSize) { EMPTY.name[0] } }

    constructor(chessBoard: ChessBoard): this(chessBoard.boardSize) {
        chessBoard.board.forEachIndexed { indexArray, chars ->
            chars.forEachIndexed { indexChar, char ->
                board[indexArray][indexChar] = char
            }
        }
    }

    companion object {
        var entityCounter = 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChessBoard

        if (boardSize != other.boardSize) return false
        if (id != other.id) return false
        if (!board.contentDeepEquals(other.board)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = boardSize
        result = 31 * result + id.hashCode()
        result = 31 * result + board.contentDeepHashCode()
        return result
    }

    override fun toString(): String {
        return "ChessBoard(boardSize=$boardSize, id=$id, board=${board.contentToString()})"
    }
}