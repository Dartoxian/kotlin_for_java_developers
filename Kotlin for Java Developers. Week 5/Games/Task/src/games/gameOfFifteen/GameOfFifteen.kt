package games.gameOfFifteen

import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

class GameOfFifteen(
    val initializer: GameOfFifteenInitializer
): Game {
    private val board: GameBoard<Int?> = createGameBoard(4)
    private var nullPosition = 4 to 4

    override fun initialize() {
        initializer.initialPermutation.forEachIndexed {index, value ->
            board[board.getCell(1 + (index / 4), 1 + (index % 4))] = value
        }
    }

    override fun canMove(): Boolean {
        return true
    }

    override fun hasWon(): Boolean =
        (0..15).map {index ->
            board[board.getCell(1 + (index / 4), 1 + (index % 4))]
        }.filterNotNull()
            .zipWithNext()
            .all { (a, b) -> a < b }

    override fun processMove(direction: Direction) {
        // Directions read inverted because of the null travels in the opposite direction.
        val targetNull = when(direction) {
            Direction.UP -> (nullPosition.first + 1) to nullPosition.second
            Direction.DOWN -> (nullPosition.first - 1) to nullPosition.second
            Direction.RIGHT -> nullPosition.first to (nullPosition.second - 1)
            Direction.LEFT -> nullPosition.first to (nullPosition.second + 1)
        }
        if (targetNull.first !in 1..4 || targetNull.second !in 1..4) {
            return
        }
        board[board.getCell(nullPosition.first, nullPosition.second)] =
            board[board.getCell(targetNull.first, targetNull.second)]
        board[board.getCell(targetNull.first, targetNull.second)] = null
        nullPosition = targetNull
    }

    override fun get(i: Int, j: Int): Int? = board[board.getCell(i, j)]
}

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteen(initializer)


