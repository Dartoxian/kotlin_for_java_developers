package nicestring

fun noBuBaBe(s: String): Boolean {
    return !(
        s.contains("bu")
        || s.contains("ba")
        || s.contains("be")
    )
}

fun Char.isVowel(): Boolean = setOf('a', 'e', 'i', 'o', 'u').contains(this)

fun String.isNice(): Boolean {
    return listOf(
            noBuBaBe(this),
            this.map(Char::isVowel).filter { it }.count() >= 3,
            this.mapIndexed { index, c -> if (index == 0) false else c == this[index - 1] }
                    .filter { it }.count() >= 1
    ).filter{it}.count() >= 2
}