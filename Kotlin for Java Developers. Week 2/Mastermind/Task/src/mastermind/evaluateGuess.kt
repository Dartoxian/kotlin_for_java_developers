package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    val (rightPositions, mismatches) = secret.zip(guess).partition { (s, g) -> s == g}
    val (secretMiss, guessMiss) = mismatches.unzip()
    val guessLookup = guessMiss.groupBy { it }
    val secretLookup = secretMiss.groupBy { it }

    return Evaluation(
            rightPosition = rightPositions.count(),
            wrongPosition = guessLookup.map { it.value.size.coerceAtMost(secretLookup.getOrDefault(it.key, listOf()).size)}.sum()
    )
}
