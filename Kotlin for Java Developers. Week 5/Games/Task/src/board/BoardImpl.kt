package board

import board.Direction.*
import java.lang.IllegalArgumentException

private class SquareBoardImpl(override val width: Int): SquareBoard {

    private val cells = (1..width).map { i -> (1..width).map { j -> Cell(i, j)}}

    override fun getCellOrNull(i: Int, j: Int): Cell? =
        if (i in 1..width && j in 1..width) cells[i - 1][j - 1] else null

    override fun getCell(i: Int, j: Int): Cell =
        getCellOrNull(i, j) ?: throw IllegalArgumentException("Expected i and j to be between 1 and $width, received $i, $j")

    override fun getAllCells(): Collection<Cell> = cells.flatten()

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> =
        jRange.mapNotNull { j -> getCellOrNull(i, j) }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> =
        iRange.mapNotNull { i -> getCellOrNull(i, j) }

    override fun Cell.getNeighbour(direction: Direction): Cell? = when(direction) {
        UP -> getCellOrNull(i - 1, j)
        RIGHT -> getCellOrNull(i, j + 1)
        DOWN -> getCellOrNull(i + 1, j)
        LEFT -> getCellOrNull(i, j - 1)
    }
}

private class GameBoardImpl<T>(override val width: Int): GameBoard<T>, SquareBoard by createSquareBoard(width) {
    private val cellValues: MutableMap<Cell, T?> = getAllCells().associateWith { null }.toMutableMap()

    override fun get(cell: Cell): T? = cellValues[cell]

    override fun set(cell: Cell, value: T?) {
        cellValues[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> =
        cellValues.filterValues(predicate).keys

    override fun find(predicate: (T?) -> Boolean): Cell? = filter(predicate).firstOrNull()

    override fun any(predicate: (T?) -> Boolean): Boolean = cellValues.values.any(predicate)

    override fun all(predicate: (T?) -> Boolean): Boolean = cellValues.values.all(predicate)

}


fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

