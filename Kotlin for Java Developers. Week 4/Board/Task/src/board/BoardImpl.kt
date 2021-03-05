package board

import board.Direction.*
import java.lang.IllegalArgumentException

internal class SquareBoardImpl(override val width: Int) : SquareBoard {
    private val cells = (1..width).map { i ->
        (1..width).map {j ->  Cell(i, j) }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        if (i !in 1..width || j !in 1..width) {
            return null
        }
        return cells[i - 1][j - 1]
    }

    override fun getCell(i: Int, j: Int): Cell {
        return getCellOrNull(i, j)
            ?: throw IllegalArgumentException("i and j must be values between 1 and $width, not ($i, $j)")
    }

    override fun getAllCells(): Collection<Cell> = cells.flatten()

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> =
        jRange.mapNotNull { j -> getCellOrNull(i, j) }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> =
        iRange.mapNotNull { i -> getCellOrNull(i, j) }

    private fun Direction.getJOffset() = when(this) {
        LEFT -> -1
        RIGHT -> 1
        else -> 0
    }

    private fun Direction.getIOffset() = when(this) {
        UP -> -1
        DOWN -> 1
        else -> 0
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? =
        getCellOrNull(this.i + direction.getIOffset(), this.j + direction.getJOffset())
}

internal class GameBoardImpl<T>(
    override val width: Int,
    private val squareBoard: SquareBoard = SquareBoardImpl(width)
): GameBoard<T>, SquareBoard by squareBoard {
    private val valueLookup = getAllCells().associateWith<Cell, T?> { null }.toMutableMap()

    override fun get(cell: Cell): T? = valueLookup[cell]

    override fun set(cell: Cell, value: T?) {
        valueLookup[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> =
        valueLookup.filterValues(predicate).keys

    override fun find(predicate: (T?) -> Boolean): Cell? =
        filter(predicate).firstOrNull()

    override fun any(predicate: (T?) -> Boolean): Boolean =
        valueLookup.values.any(predicate)

    override fun all(predicate: (T?) -> Boolean): Boolean =
        valueLookup.values.all(predicate)

}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

