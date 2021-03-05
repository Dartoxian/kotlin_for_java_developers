package games.gameOfFifteen

interface GameOfFifteenInitializer {
    /*
     * Even permutation of numbers 1..15
     * used to initialized the first 15 cells on a board.
     * The last cell is empty.
     */
    val initialPermutation: List<Int>
}

class RandomGameInitializer : GameOfFifteenInitializer {
    /*
     * Generate a random permutation from 1 to 15.
     * `shuffled()` function might be helpful.
     * If the permutation is not even, make it even (for instance,
     * by swapping two numbers).
     */
    override val initialPermutation by lazy {
        val permutation = (1..15).shuffled().toMutableList()
        if (!isEven(permutation)) {
            val swapIndex = permutation.indexOfFirst { i -> permutation[i] > permutation[i + 1] }
            val t = permutation[swapIndex]
            permutation[swapIndex] = permutation[swapIndex + 1]
            permutation[swapIndex + 1] = t
        }
        permutation
    }
}

