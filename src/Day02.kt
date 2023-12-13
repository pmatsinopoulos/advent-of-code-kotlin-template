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
)

data class Game(
    val index: Int,
    val sets: List<SetOfCubes>
)

data class Input(
    val red: Int,
    val green: Int,
    val blue: Int
)

fun buildSetOfCubes(input: String): SetOfCubes {
    val numbersForColors = input.split(",").map { it.trim() }
    val setOfCubes = numbersForColors.fold(SetOfCubes()) { acc, numberForColor ->
        val (number, color) = numberForColor.split(" ").map { it.trim() }
        acc.numbersForColors[Color.valueOf(color.uppercase())] = number.toInt()
        acc
    }
    return setOfCubes
}

fun buildGame(input: String): Game {
    val (game, setsString) = input.split(":").map { it.trim() }
    val gameIndex = game.replace("Game ", "").toInt()
    val sets = setsString.split(";").map { set -> set.trim() }
    val setsOfCubes = sets.map { s -> buildSetOfCubes(s) }

    return Game(index = gameIndex, sets = setsOfCubes)
}

fun possibleSet(set: SetOfCubes, input: Input): Boolean {
    return set.numbersForColors.getOrDefault(Color.RED, 0) <= input.red &&
            set.numbersForColors.getOrDefault(Color.GREEN, 0) <= input.green &&
            set.numbersForColors.getOrDefault(Color.BLUE, 0) <= input.blue
}

fun possibleGame(game: Game, input: Input): Boolean {
    return game.sets.all { set -> possibleSet(set, input) }
}

fun powerOfColor(set: SetOfCubes, color: Color): Int {
    return set.numbersForColors.getOrDefault(color, 1)
}

fun maxPowerOfColor(game: Game, color: Color): Int {
    return game.sets.map { set -> powerOfColor(set, color) }.max()
}

fun powerOfGame(game: Game): Int {
    return maxPowerOfColor(game, Color.RED) * maxPowerOfColor(game, Color.GREEN) * maxPowerOfColor(game, Color.BLUE)
}

fun main() {
    val input = Input(red = 12, green = 13, blue = 14)
    val resultPart1 = readInput("Day02").sumOf { inputGame ->
        val game = buildGame(inputGame)
        if (possibleGame(game, input)) {
            game.index
        } else {
            0
        }
    }

    println("part 1 result = $resultPart1") // Should be 2447

    val resultPart2 = readInput("Day02").sumOf { inputGame ->
        val game = buildGame(inputGame)
        powerOfGame(game)
    }

    print("part 2 result = $resultPart2") // Should be 56322
}
