val ZERO_TO_DIGITS = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9"
)

private fun calibrate(s: String): Int = "${s.first { it.isDigit() }}${s.last { it.isDigit() }}".toInt()

private fun calibrate2(s: String): Int {
    return calibrate(s.mapIndexedNotNull { index, c ->
        if (c.isDigit()) {
            c
        } else {
            s.possibleWordsAtIndex(index).firstNotNullOfOrNull { candidate ->
                ZERO_TO_DIGITS[candidate]
            }
        }
    }.joinToString())
}

private fun String.possibleWordsAtIndex(index: Int): List<String> {
    return (3..5).map { len ->
        substring(index, (index + len).coerceAtMost(length))
    }
}

// 53551
fun main() {
    fun part1(input: List<String>): Int = input.sumOf { s -> calibrate(s) }

    fun part2(input: List<String>): Int = input.sumOf { s -> calibrate2(s) }

    // test if implementation meets criteria from the description, like:
    var testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    testInput = readInput("Day01_test2")
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
