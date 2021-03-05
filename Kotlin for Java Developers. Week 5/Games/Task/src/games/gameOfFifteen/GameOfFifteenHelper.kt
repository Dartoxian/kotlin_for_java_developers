package games.gameOfFifteen

/*
 * This function should return the parity of the permutation.
 * true - the permutation is even
 * false - the permutation is odd
 * https://en.wikipedia.org/wiki/Parity_of_a_permutation

 * If the game of fifteen is started with the wrong parity, you can't get the correct result
 *   (numbers sorted in the right order, empty cell at last).
 * Thus the initial permutation should be correct.
 */
fun isEven(permutation: List<Int>): Boolean = permutation
    .mapIndexed { i, p_i ->
        permutation.slice(i until permutation.size).mapIndexed { j, p_j ->
            if (p_i > p_j) 1 else 0
        }
    }.flatten().sum() % 2 == 0