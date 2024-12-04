fun main() {
    val testInput = readInput("data/day04/sample.day04")
    val test = LetterGrid(testInput)
    check(test.findOccurrences() == 18) { "findOccurrences - Found ${test.findOccurrences()}" }
    check(test.findMasCross() == 9) { "findMasCross - Found ${test.findOccurrences()}" }

    val input = readInput("data/day04/actual.day04")
    val actual = LetterGrid(input)
    actual.findOccurrences().println()
    actual.findMasCross().println()
}


private class LetterGrid(
    input: List<String>,
) {

    private val grid: Array<Array<Char>> = input.map { it.toCharArray().toTypedArray() }.toTypedArray()
    private val rows = grid.size
    private val cols = grid[0].size

    fun findOccurrences(): Int {
        var count = 0
        val word = "XMAS"

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                if (grid[row][col] == 'X') {
                    if (searchHorizontalRight(row, col, word)) count++
                    if (searchHorizontalLeft(row, col, word)) count++
                    if (searchVerticalDown(row, col, word)) count++
                    if (searchVerticalUp(row, col, word)) count++
                    if (searchDiagonalDownRight(row, col, word)) count++
                    if (searchDiagonalDownLeft(row, col, word)) count++
                    if (searchDiagonalUpRight(row, col, word)) count++
                    if (searchDiagonalUpLeft(row, col, word)) count++
                }
            }
        }

        return count
    }

    fun findMasCross(): Int {
        var count = 0

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                if (grid[row][col] == 'A') {
                    if (searchMasLeftTopToRightBottom(row, col) && searchMasLeftBottomToRightTop(row, col)) count++
                }
            }
        }

        return count
    }

    private fun searchHorizontalRight(row: Int, col: Int, word: String) = checkDirection(row, col, word, STAY, RIGHT)
    private fun searchHorizontalLeft(row: Int, col: Int, word: String) = checkDirection(row, col, word, STAY, LEFT)
    private fun searchVerticalDown(row: Int, col: Int, word: String) = checkDirection(row, col, word, DOWN, STAY)
    private fun searchVerticalUp(row: Int, col: Int, word: String) = checkDirection(row, col, word, UP, STAY)
    private fun searchDiagonalDownRight(row: Int, col: Int, word: String) = checkDirection(row, col, word, DOWN, RIGHT)
    private fun searchDiagonalDownLeft(row: Int, col: Int, word: String) = checkDirection(row, col, word, DOWN, LEFT)
    private fun searchDiagonalUpRight(row: Int, col: Int, word: String) = checkDirection(row, col, word, UP, RIGHT)
    private fun searchDiagonalUpLeft(row: Int, col: Int, word: String) = checkDirection(row, col, word, UP, LEFT)

    private fun searchMasLeftTopToRightBottom(row: Int, col: Int): Boolean {
        return (searchDiagonalUpLeft(row, col, "AM") && searchDiagonalDownRight(row, col, "AS")) ||
            (searchDiagonalUpLeft(row, col, "AS") && searchDiagonalDownRight(row, col, "AM"))
    }

    private fun searchMasLeftBottomToRightTop(row: Int, col: Int): Boolean {
        return (searchDiagonalDownLeft(row, col, "AM") && searchDiagonalUpRight(row, col, "AS")) ||
            (searchDiagonalDownLeft(row, col, "AS") && searchDiagonalUpRight(row, col, "AM"))
    }

    private fun checkDirection(row: Int, col: Int, word: String, directionRow: Int, directionCol: Int): Boolean {
        for (i in word.indices) {
            val newRow = row + i * directionRow
            val newCol = col + i * directionCol

            // out of bounds check
            if (newRow !in 0 until rows || newCol !in 0 until cols) return false

            // check if letter is present
            if (grid[newRow][newCol] != word[i]) return false
        }
        return true
    }

    companion object {
        const val STAY = 0
        const val DOWN = 1
        const val UP = -1
        const val LEFT = -1
        const val RIGHT = 1
    }

}
