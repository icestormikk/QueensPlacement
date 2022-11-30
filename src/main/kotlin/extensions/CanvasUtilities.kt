package extensions

import domain.CellType
import domain.ChessBoard
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

object CanvasUtilities {
    private lateinit var graphicsContext: GraphicsContext

    fun initiateContext(context: GraphicsContext) {
        graphicsContext = context
    }

    fun drawChessBoard(chess: ChessBoard) {
        if (!this::graphicsContext.isInitialized)
            error("You can't draw until you initialize the graphics context.")

        val labyrinthLength = minOf(graphicsContext.canvas.width, graphicsContext.canvas.height) * 0.8
        val cellSize = labyrinthLength / chess.boardSize
        val startXCord = graphicsContext.canvas.width / 2 - labyrinthLength / 2
        val startYCord = graphicsContext.canvas.height / 2 - labyrinthLength / 2
        graphicsContext.font = Font.font("Calibri", FontWeight.BOLD, cellSize * 0.8)

        with(graphicsContext) {
            clearRect(0.0,0.0, canvas.width, canvas.height)
            strokeRect(
                startXCord,
                startYCord,
                labyrinthLength,
                labyrinthLength,
            )

            chess.board.forEachIndexed { arrayIndex, array ->
                array.forEachIndexed { cellIndex, cell ->
                    fill = getFillForCellByIndexes(arrayIndex, cellIndex)
                    fillRect(
                        startXCord + cellIndex * cellSize,
                        startYCord + arrayIndex * cellSize,
                        cellSize,
                        cellSize
                    )
                    if (cell == CellType.QUEEN.name[0]) {
                        fill = Color.BLACK
                        fillText(
                            CellType.QUEEN.name[0].toString(),
                            startXCord + cellIndex * cellSize + cellSize / 5,
                            startYCord + arrayIndex * cellSize + cellSize * 0.75,
                        )
                    }
                }
            }
        }
    }

    private fun getFillForCellByIndexes(arrayIndex: Int, cellIndex: Int): Paint =
        if (arrayIndex % 2 == 0) {
            if (cellIndex % 2 == 0) Color.WHITE else Color.DARKGRAY
        } else {
            if (cellIndex % 2 == 0) Color.DARKGRAY else Color.WHITE
        }
}