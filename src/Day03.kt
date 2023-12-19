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

fun digitsToNumber(
    matrix: Array<CharArray>,
    startDigitPosition: Pair<Int, Int>,
    endDigitPosition: Pair<Int, Int>
): Int {
    return matrix[startDigitPosition.first].slice(startDigitPosition.second..endDigitPosition.second).joinToString("")
        .toInt()
}

fun main() {
    val inputs = listOf("Day03.txt")
    for (input in inputs) {
        val matrix = buildMatrix(input)
        var sum = 0
        matrix.forEachIndexed { rowIndex, chars ->
            val digitPositions = mutableListOf<Pair<Int, Int>>()
            var inDigit = false
            chars.forEachIndexed { columnIndex, c ->
                if (c.isDot()) {
                    if (inDigit) {
                        digitPositions.add(Pair(rowIndex, columnIndex - 1)) // adding the end digit position
                        inDigit = false
                    }
                } else if (c.isDigit()) {
                    if (inDigit) {
                        if (columnIndex == chars.size - 1) {
                            // we are at the end of the row
                            digitPositions.add(Pair(rowIndex, columnIndex)) // adding the end digit position
                            inDigit = false
                        }
                    } else {
                        digitPositions.add(Pair(rowIndex, columnIndex)) // adding the start digit position
                        inDigit = true
                    }
                } else if (c.isSymbol()) {
                    if (inDigit) {
                        digitPositions.add(Pair(rowIndex, columnIndex - 1)) // adding the end digit position
                        inDigit = false
                    }
                }
            }
            // here, I have all the pairs of beginnings and endings of numbers
            var i = 0
            while (i < digitPositions.size) {

                if (numberHasNeighbouringSymbol(matrix, digitPositions[i], digitPositions[i + 1])) {
                    // need to convert digits to number and add
                    sum += digitsToNumber(matrix, digitPositions[i], digitPositions[i + 1])
                }


                i += 2
            }
        }
        println(sum)
    }
}
