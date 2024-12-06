fun main() {
    val testInput = readInput("data/day06/sample.day06")
    val test = GuardPatrol(testInput)
    val testDistinctPositionsCount = test.getDistinctPositionsCount()
    check(testDistinctPositionsCount == 41) { "getDistinctPositionsCount - Found $testDistinctPositionsCount" }
    val test2 = LoopFinder(testInput)
    val testFindObstacles = test2.findObstacles()
    check(testFindObstacles == 6L) { "getSumOfCenterOfCorrectlyOrdered - Found ${testFindObstacles}" }

    val input = readInput("data/day06/actual.day06")
    val actual = GuardPatrol(input)
    actual.getDistinctPositionsCount().println()
    val actual2 = LoopFinder(input)
    actual2.findObstacles().println()
}

private data class Position(val row: Int, val col: Int)
private data class Guard(val position: Position, val direction: Direction)

private class LoopFinder(val input: List<String>) {
    private val grid: Array<Array<Char>> = input.map { it.toCharArray().toTypedArray() }.toTypedArray()
    fun findObstacles(): Long {
        return (0 until grid.size).toList().parallelStream().flatMap { row ->
            (0 until grid[0].size).toList().parallelStream().filter { col ->
                try {
                    val modifiedInput = input.toMutableList()
                    val modifiedLine = modifiedInput[row].toCharArray()

                    if(modifiedLine[col] != '.') return@filter false

                    modifiedLine[col] = 'O'
                    modifiedInput[row] = modifiedLine.joinToString("")
                    GuardPatrol(modifiedInput).getDistinctPositionsCount()
                    return@filter false
                } catch (e: Error) {
                    return@filter true
                }
            }
        }.count()
    }
}

private class GuardPatrol(
    input: List<String>,
) {

    private val grid: Array<Array<Char>> = input.map { it.toCharArray().toTypedArray() }.toTypedArray()
    private val rows = grid.size
    private val cols = grid[0].size

    private val visitedPositions = mutableSetOf<Position>()
    private val visitedGuardMoments = mutableSetOf<Guard>()

    fun getDistinctPositionsCount(): Int {
        visitedPositions.clear()
        walkTillTheEnd(Guard(findStart(), Direction.UP))
        return visitedPositions.size
    }


    private fun findStart(): Position {
        grid.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, char ->
                if (char == '^') return Position(rowIndex, columnIndex)
            }
        }
        error("No starting position")
    }

    private fun walkTillTheEnd(guard: Guard): Guard {
        var newGuard: Guard = guard
        do {
            newGuard = walk(newGuard)
        } while (!newGuard.position.isOutOfBounds())
        return newGuard
    }

    private fun walk(guard: Guard): Guard {
        val originalPosition = guard.position
        visitedPositions.add(originalPosition)
        if (visitedGuardMoments.contains(guard)) throw Error("LOOP!")
        visitedGuardMoments.add(guard)

        var direction = guard.direction

        grid[originalPosition.row][originalPosition.col] = direction.toChar()

//        printField()

        var newPosition = originalPosition.move(direction)
        var count = 0

        while (newPosition.isOutOfBounds() || newPosition.isWall()) {
            if (count++ > 4) error("Stuck!")
            if (newPosition.isOutOfBounds()) {
                return Guard(newPosition, direction)
            }
            if (newPosition.isWall()) {
                direction = direction.rotateRight()
                newPosition = originalPosition.move(direction)
            }
        }
        grid[originalPosition.row][originalPosition.col] = 'X'
        return Guard(newPosition, direction)
    }

    private fun Position.move(direction: Direction): Position {
        return when (direction) {
            Direction.UP -> Position(this.row - 1, this.col)
            Direction.DOWN -> Position(this.row + 1, this.col)
            Direction.LEFT -> Position(this.row, this.col - 1)
            Direction.RIGHT -> Position(this.row, this.col + 1)
        }
    }

    private fun printField() {
        println("-----------------------------")
        grid.forEach { row ->
            row.forEach { field ->
                print(field)
            }
            println("")
        }
        println("-----------------------------")
    }

    private fun Position.isOutOfBounds(): Boolean = this.row < 0 || this.col < 0 || this.row >= rows || this.col >= cols
    private fun Position.isWall(): Boolean = grid[this.row][this.col] == '#' || grid[this.row][this.col] == 'O'

    private fun Direction.rotateRight(): Direction {
        return when (this) {
            Direction.UP -> Direction.RIGHT
            Direction.RIGHT -> Direction.DOWN
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.UP
        }
    }

    private fun Direction.toChar(): Char {
        return when (this) {
            Direction.UP -> '^'
            Direction.RIGHT -> '>'
            Direction.DOWN -> 'V'
            Direction.LEFT -> '<'
        }
    }
}

private enum class Direction {
    UP, DOWN, LEFT, RIGHT
}