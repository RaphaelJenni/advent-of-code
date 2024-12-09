fun main() {
    val testInput = readInput("data/day08/sample.day08")
    val test = AntennaDestroyer(testInput, true)

    val countAntinodesForTower = test.countAntinodesForTower()
    check(countAntinodesForTower == 14) { "countAntinodesForTower - Found $countAntinodesForTower" }

    val test2 = AntennaDestroyer(testInput, false)

    val countAntinodesForTower2 = test2.countAntinodesForTower()
    check(countAntinodesForTower2 == 34) { "countAntinodesForTower - Found $countAntinodesForTower2" }

    val input = readInput("data/day08/actual.day08")
    val actual = AntennaDestroyer(input, true)
    actual.countAntinodesForTower().println()
    val actual2 = AntennaDestroyer(input, false)
    actual2.countAntinodesForTower().println()
}

private class AntennaDestroyer(
    input: List<String>,
    private val part1: Boolean = true,
) {
    private val grid: Array<Array<Char>> = input.map { it.toCharArray().toTypedArray() }.toTypedArray()
    private val rows = grid.size
    private val cols = grid[0].size

    private var antiNodesCount: MutableSet<Pair<Int, Int>> = mutableSetOf()

    init {
        addAntinodes()
    }

    fun addAntinodes() {
//        println("before:")
//        printMap()

        for ((rowI, row) in grid.withIndex()) {
            for ((colI, cell) in row.withIndex()) {
                if (Regex("[0-9a-zA-Z]").matches(cell.toString())) {
                    addAntinodesForTower(rowI, colI)
                }
            }
        }

//        println("after:")
//        printMap()
    }

    fun addAntinodesForTower(rowI: Int, colI: Int) {
        val towerFrequency = grid[rowI][colI]

        for (searchRowI in rowI until rows) {
            val colStart = if (searchRowI == rowI) colI + 1 else 0
            for (searchColI in colStart until cols) {
                if (grid[searchRowI][searchColI] == towerFrequency) {
                    val rowDelta = searchRowI - rowI
                    val colDelta = searchColI - colI

                    addAntinodesInDirectionFromTower(rowI, colI, searchRowI, searchColI, rowDelta, colDelta)

//                    val antiAboveRow = rowI - rowDelta
//                    val antiAboveCol = colI - colDelta
//
//                    val antiBelowRow = searchRowI + rowDelta
//                    val antiBelowCol = searchColI + colDelta
//
//                    if (antiAboveRow in 0 until rows && antiAboveCol in 0 until cols) {
//                        antiNodesCount.add(antiAboveRow to antiAboveCol)
//                    }
//                    if (antiBelowRow in 0 until rows && antiBelowCol in 0 until cols) {
//                        antiNodesCount.add(antiBelowRow to antiBelowCol)
//                    }
                }
            }
        }
    }

    fun addAntinodesInDirectionFromTower(rowI: Int, colI: Int, searchRowI: Int, searchColI: Int, rowDelta: Int, colDelta: Int) {
        if (!part1) {
            antiNodesCount.add(rowI to colI)
            antiNodesCount.add(searchRowI to searchColI)
        }

        var antiAboveRow = rowI - rowDelta
        var antiAboveCol = colI - colDelta

        while (antiAboveRow in 0 until rows && antiAboveCol in 0 until cols) {
            antiNodesCount.add(antiAboveRow to antiAboveCol)

            if (part1) break

            antiAboveRow = antiAboveRow - rowDelta
            antiAboveCol = antiAboveCol - colDelta
        }

        var antiBelowRow = searchRowI + rowDelta
        var antiBelowCol = searchColI + colDelta
        while (antiBelowRow in 0 until rows && antiBelowCol in 0 until cols) {
            antiNodesCount.add(antiBelowRow to antiBelowCol)

            if (part1) break

            antiBelowRow = antiBelowRow + rowDelta
            antiBelowCol = antiBelowCol + colDelta
        }
    }

    fun countAntinodesForTower(): Int {
        return antiNodesCount.count()
    }

    private fun printMap() {
        println("-----------------------------")
        grid.forEach { row ->
            row.forEach { field ->
                print(field)
            }
            println("")
        }
        println("-----------------------------")
    }

}