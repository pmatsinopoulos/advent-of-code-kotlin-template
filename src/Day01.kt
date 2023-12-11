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

fun calibrate(s: String): Int = "${s.first { it.isDigit() }}${s.last { it.isDigit() }}".toInt()

fun calibrate2(s: String): Int {
    var ss = s
    var position = 0
    while (position <= ss.length - 1) {
        for (number in ZERO_TO_DIGITS.keys) {
            if (ss.substring(position, (position + number.length).coerceAtMost(ss.length)) == number) {
                ss = ss.replaceRange(position, position + number.length, ZERO_TO_DIGITS[number] ?: "")
                break
            }
        }
        position += 1
    }
    return calibrate(ss)
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

    testInput = readInput("Day01_test2_2")
    check(
        part2(testInput) ==
                55 +
                97 +
                26 +
                55 +
                27 +
                82 +
                56 +
                23 +
                14 +
                97 +
                65 +
                23 +
                18 +
                71 + // 14
                63 + // 15
                15 +
                88 +
                88 +
                83 +
                71 // 20
                + 15
                + 88
                + 55
                + 55
    )

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
