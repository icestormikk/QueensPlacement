package views

import domain.ChessBoard
import extensions.CanvasUtilities
import extensions.ChessUtilities
import extensions.RECURSIVE_ALGORITHM_LIMIT
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import tornadofx.View
import tornadofx.onChange
import tornadofx.onLeftClick

const val ID_DELIMITER = "#"

class MainView : View("Queens Placement") {
    override val root: BorderPane by fxml()

    private val chessSizeLeft: Label by fxid()
    private val chessSizeRight: Label by fxid()
    private val changeChessBoardSize: Button by fxid()
    private val newChessBoardSize: Spinner<Int> by fxid()
    private val possibleVariantsList: ListView<String> by fxid()
    private val additionalInfo: Label by fxid()

    private lateinit var resizableCanvas: ResizableCanvas
    private lateinit var chessBoard: ChessBoard

    init {
        currentStage?.icons?.add(
            Image("icons/chess-queen-solid.svg")
        )

        configureCanvas()
        configureChessBoard()
        configureButtons()
        configureLabels()

        possibleVariantsList.selectionModel.selectedItemProperty().onChange {
            if (it != null) {
                drawSelectedChessBoard(
                    it.substringAfter(ID_DELIMITER).toInt()
                )
            }
        }
    }

    private fun configureButtons() {
        newChessBoardSize.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(4, Int.MAX_VALUE, 8)
        changeChessBoardSize.onLeftClick {
            configureChessBoard(newChessBoardSize.value)
            drawSelectedChessBoard(0)
        }
    }

    private fun configureChessBoard(boardSize: Int = 8) {
        chessBoard = ChessBoard(boardSize)

        ChessUtilities.placeNQueens(chessBoard)
        configureLabels()
        resizableCanvas.autosize()

        possibleVariantsList.items.clear()
        ChessUtilities.possibleVariants.map { "Вариант $ID_DELIMITER${it.key}" }.forEach {
            with (possibleVariantsList.items) {
                if (!contains(it)) add(it)
            }
        }
    }

    private fun drawSelectedChessBoard(index: Int) {
        CanvasUtilities.drawChessBoard(
            ChessUtilities.possibleVariants[index]
                ?: error("It looks like you wanted to draw a chessboard before initializing the list of boards")
        )
    }

    private fun configureLabels() {
        chessSizeLeft.text = "${chessBoard.boardSize}"
        chessSizeRight.text = "${chessBoard.boardSize}"
        with (chessBoard.boardSize) {
            additionalInfo.text = if (this > RECURSIVE_ALGORITHM_LIMIT) "Количество вариантов размещения для доски размером $this x $this слишком велико. " +
                    "Показан лишь один из возможных вариантов." else ""
        }
    }

    private fun configureCanvas() {
        resizableCanvas = ResizableCanvas()
        CanvasUtilities.initiateContext(resizableCanvas.graphicsContext2D)

        with (resizableCanvas) {
            widthProperty().bind(root.widthProperty())
            heightProperty().bind(root.heightProperty())
        }

        root.center.add(resizableCanvas)
    }

    inner class ResizableCanvas: Canvas() {
        override fun isResizable(): Boolean =
            true

        override fun minWidth(height: Double): Double =
            1.0

        override fun minHeight(width: Double): Double =
            1.0

        override fun maxWidth(height: Double): Double =
            root.maxWidth

        override fun maxHeight(width: Double): Double =
            root.maxHeight

        override fun resize(width: Double, height: Double) {
            CanvasUtilities.drawChessBoard(chessBoard)
        }
    }
}