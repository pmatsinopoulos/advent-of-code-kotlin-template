// I need to parse one line and retrieve the following information:
// Game index
// Number of Sets
// For Each Set the number of blue, red, and green cubes
// All sets in a game should be possible for the game to be possible.
//

enum class Color {
    RED,
    GREEN,
    BLUE
}

data class SetOfCubes(
    val numbersForColors: MutableMap<Color, Int> = mutableMapOf() // e.g. RED -> 4, GREEN -> 5, BLUE -> 6
) {
    fun possible(input: Input): Boolean = numbersForColors.getOrDefault(Color.RED, 0) <= input.red &&
            numbersForColors.getOrDefault(Color.GREEN, 0) <= input.green &&
            numbersForColors.getOrDefault(Color.BLUE, 0) <= input.blue

    fun powerOfColor(color: Color): Int = numbersForColors.getOrDefault(color, 1)

    companion object {
        fun build(input: String): SetOfCubes {
            val numbersForColors = input.split(",").map { it.trim() }
            val setOfCubes = numbersForColors.fold(SetOfCubes()) { acc, numberForColor ->
                val (number, color) = numberForColor.split(" ").map { it.trim() }
                acc.numbersForColors[Color.valueOf(color.uppercase())] = number.toInt()
                acc
            }
            return setOfCubes
        }
    }
}

data class Game(
    val index: Int,
    val sets: List<SetOfCubes>
) {
    fun possible(input: Input): Boolean = sets.all { set -> set.possible(input) }

    fun power(): Int =

        maxPowerOfColor(Color.RED) * maxPowerOfColor(Color.GREEN) * maxPowerOfColor(Color.BLUE)

    private fun maxPowerOfColor(color: Color): Int = sets.map { set -> set.powerOfColor(color) }.max()

    companion object {
        fun build(input: String): Game {
            val (game, setsString) = input.split(":").map { it.trim() }
            val gameIndex = game.replace("Game ", "").toInt()
            val sets = setsString.split(";").map { set -> set.trim() }
            val setsOfCubes = sets.map { s -> SetOfCubes.build(s) }

            return Game(index = gameIndex, sets = setsOfCubes)
        }
    }
}

data class Input(
    val red: Int,
    val green: Int,
    val blue: Int
)

fun main() {
    val input = Input(red = 12, green = 13, blue = 14)
    val resultPart1 = readInput("Day02").sumOf { inputGame ->
        val game = Game.build(inputGame)
        if (game.possible(input)) {
            game.index
        } else {
            0
        }
    }

    println("part 1 result = $resultPart1") // Should be 2447

    val resultPart2 = readInput("Day02").sumOf { inputGame ->
        val game = Game.build(inputGame)
        game.power()
    }

    print("part 2 result = $resultPart2") // Should be 56322
}
