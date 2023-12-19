import kotlin.io.path.Path
import kotlin.io.path.readLines

fun buildMatrix(inputFile: String): Array<CharArray> {
    val fileLines = Path("src/$inputFile").readLines()
    val numberOfColumns = fileLines.first().length
    val matrix = Array(fileLines.size) { rowIndex ->
        CharArray(numberOfColumns) { columnIndex ->
            fileLines[rowIndex][columnIndex]
        }
    }
    return matrix
}

fun numberHasNeighbouringSymbol(
    matrix: Array<CharArray>,
    startDigitPosition: Pair<Int, Int>,
    endDigitPosition: Pair<Int, Int>
): Boolean {
    val result = false
    var startScan = startDigitPosition.second - 1
    if (startScan < 0) {
        startScan = 0
    }
    var endScan = endDigitPosition.second + 1
    val numberOfColumns = matrix.first().size
    val numberOfRows = matrix.size
    if (endScan >= numberOfColumns) {
        endScan = numberOfColumns - 1
    }
    val rowIndex = startDigitPosition.first
    for (scanPosition in startScan..endScan) {
        if (matrix[rowIndex][scanPosition].isSymbol()) {
            return true
        }
        if (rowIndex > 0 && matrix[rowIndex - 1][scanPosition].isSymbol()) {
            return true
        }
        if (rowIndex < numberOfRows - 1 && matrix[rowIndex + 1][scanPosition].isSymbol()) {
            return true
        }
    }
    return result
}

fun neighbouringGears(
    matrix: Array<CharArray>,
    number: NumberInMatrix
): MutableList<Pair<Int, Int>> {
    // A number may have many Gear neighbours
    val result = mutableListOf<GearPosition>()

    val startDigitPosition = number.first
    var startScan = startDigitPosition.second - 1
    if (startScan < 0) {
        startScan = 0
    }

    val endDigitPosition = number.second
    var endScan = endDigitPosition.second + 1
    val numberOfColumns = matrix.first().size
    val numberOfRows = matrix.size
    if (endScan >= numberOfColumns) {
        endScan = numberOfColumns - 1
    }

    val rowIndex = startDigitPosition.first
    for (scanPosition in startScan..endScan) {
        if (matrix[rowIndex][scanPosition].isGear()) {
            result.add(GearPosition(rowIndex, scanPosition))
        }
        if (rowIndex > 0 && matrix[rowIndex - 1][scanPosition].isGear()) {
            result.add(GearPosition(rowIndex - 1, scanPosition))
        }
        if (rowIndex < numberOfRows - 1 && matrix[rowIndex + 1][scanPosition].isGear()) {
            result.add(GearPosition(rowIndex + 1, scanPosition))
        }
    }
    return result
}

fun digitsToNumber(
    matrix: Array<CharArray>,
    number: NumberInMatrix
): Int {
    val startDigitPosition = number.first
    val endDigitPosition = number.second
    return matrix[startDigitPosition.first].slice(startDigitPosition.second..endDigitPosition.second).joinToString("")
        .toInt()
}

typealias Position = Pair<Int, Int>
typealias GearPosition = Position
typealias DigitPosition = Position
typealias StartPosition = DigitPosition
typealias EndPosition = DigitPosition
typealias NumberInMatrix = Pair<StartPosition, EndPosition>

fun gearRatio(matrix: Array<CharArray>, numbers: MutableList<NumberInMatrix>): Int {
    return numbers.fold(1) { acc, number ->
        acc * digitsToNumber(matrix, number)
    }
}

fun main() {
    val inputs = listOf("Day03.txt")
    for (input in inputs) {
        val matrix = buildMatrix(input)
        var sum = 0
        val numbersForGears: MutableMap<
                GearPosition, // Gear position
                MutableList<NumberInMatrix> // numbers neighbouring with gear position
                > = mutableMapOf()
        matrix.forEachIndexed { rowIndex, chars ->
            val digitPositions =
                mutableListOf<DigitPosition>() // an even set of digit positions, each pair representing the start and end position of a number
            var inDigit = false
            chars.forEachIndexed { columnIndex, c ->
                if (c.isDot()) {
                    if (inDigit) {
                        digitPositions.add(EndPosition(rowIndex, columnIndex - 1)) // adding the end digit position
                        inDigit = false
                    }
                } else if (c.isDigit()) {
                    if (inDigit) {
                        if (columnIndex == chars.size - 1) {
                            // we are at the end of the row
                            digitPositions.add(EndPosition(rowIndex, columnIndex)) // adding the end digit position
                            inDigit = false
                        }
                    } else {
                        digitPositions.add(StartPosition(rowIndex, columnIndex)) // adding the start digit position
                        inDigit = true
                    }
                } else if (c.isSymbol()) {
                    if (inDigit) {
                        digitPositions.add(EndPosition(rowIndex, columnIndex - 1)) // adding the end digit position
                        inDigit = false
                    }
                }
            }
            // here, I have all the pairs of beginnings and endings of numbers
            var i = 0
            while (i < digitPositions.size) {
                val numberInMatrix = NumberInMatrix(digitPositions[i], digitPositions[i + 1])
                val neighbouringGearsForNumber = neighbouringGears(matrix, numberInMatrix)
                neighbouringGearsForNumber.forEach { gearForNumber ->
                    if (numbersForGears[gearForNumber] == null) {
                        numbersForGears[gearForNumber] = mutableListOf()
                    }
                    numbersForGears[gearForNumber]?.add(numberInMatrix)
                }

                i += 2
            }
        }
        sum = numbersForGears.filterValues { numbers -> numbers.size >= 2 }
            .values.map { numbers ->
                gearRatio(matrix, numbers)
            }.sum()
        println(sum)
    }
}
