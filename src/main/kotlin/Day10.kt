fun main() {
    val testInput = readInput("data/day10/sample01.day10")
    val test1 = HikingTail(testInput)
    val distinctPositionsCount = test1.getDistinctPositionsCount()
    check(distinctPositionsCount == 1) { "Test 1 failed. Expected 1 but got $distinctPositionsCount" }

    val testInput2 = readInput("data/day10/sample02.day10")
    val test2 = HikingTail(testInput2)
    val distinctPositionsCount2 = test2.getDistinctPositionsCount()
    val rating2 = test2.getRating()
    check(distinctPositionsCount2 == 36) { "Test 2 failed. Expected 36 but got $distinctPositionsCount2" }
    check(rating2 == 81) { "Test 2 failed. Expected 81 but got $rating2" }


    val input = readInput("data/day10/actual.day10")
    val hikingTail = HikingTail(input)
    val result = hikingTail.getDistinctPositionsCount()
    result.println()
    val result2 = hikingTail.getRating()
    result2.println()
}

/**
 * You all arrive at a Lava Production Facility on a floating island in the sky. As the others begin to search the massive industrial complex, you feel a small nose boop your leg and look down to discover a reindeer wearing a hard hat.
 *
 * The reindeer is holding a book titled "Lava Island Hiking Guide". However, when you open the book, you discover that most of it seems to have been scorched by lava! As you're about to ask how you can help, the reindeer brings you a blank topographic map of the surrounding area (your puzzle input) and looks up at you excitedly.
 *
 * Perhaps you can help fill in the missing hiking trails?
 *
 * The topographic map indicates the height at each position using a scale from 0 (lowest) to 9 (highest). For example:
 *
 * 0123
 * 1234
 * 8765
 * 9876
 * Based on un-scorched scraps of the book, you determine that a good hiking trail is as long as possible and has an even, gradual, uphill slope. For all practical purposes, this means that a hiking trail is any path that starts at height 0, ends at height 9, and always increases by a height of exactly 1 at each step. Hiking trails never include diagonal steps - only up, down, left, or right (from the perspective of the map).
 *
 * You look up from the map and notice that the reindeer has helpfully begun to construct a small pile of pencils, markers, rulers, compasses, stickers, and other equipment you might need to update the map with hiking trails.
 *
 * A trailhead is any position that starts one or more hiking trails - here, these positions will always have height 0. Assembling more fragments of pages, you establish that a trailhead's score is the number of 9-height positions reachable from that trailhead via a hiking trail. In the above example, the single trailhead in the top left corner has a score of 1 because it can reach a single 9 (the one in the bottom left).
 *
 * This trailhead has a score of 2:
 *
 * ...0...
 * ...1...
 * ...2...
 * 6543456
 * 7.....7
 * 8.....8
 * 9.....9
 * (The positions marked . are impassable tiles to simplify these examples; they do not appear on your actual topographic map.)
 *
 * This trailhead has a score of 4 because every 9 is reachable via a hiking trail except the one immediately to the left of the trailhead:
 *
 * ..90..9
 * ...1.98
 * ...2..7
 * 6543456
 * 765.987
 * 876....
 * 987....
 * This topographic map contains two trailheads; the trailhead at the top has a score of 1, while the trailhead at the bottom has a score of 2:
 *
 * 10..9..
 * 2...8..
 * 3...7..
 * 4567654
 * ...8..3
 * ...9..2
 * .....01
 * Here's a larger example:
 *
 * 89010123
 * 78121874
 * 87430965
 * 96549874
 * 45678903
 * 32019012
 * 01329801
 * 10456732
 * This larger example has 9 trailheads. Considering the trailheads in reading order, they have scores of 5, 6, 5, 3, 1, 3, 5, 3, and 5. Adding these scores together, the sum of the scores of all trailheads is 36.
 *
 * The reindeer gleefully carries over a protractor and adds it to the pile. What is the sum of the scores of all trailheads on your topographic map?
 */
class HikingTail(
    val input: List<String>
) {
    private val grid: Array<Array<Char>> = input.map { it.toCharArray().toTypedArray() }.toTypedArray()
    private val rows = grid.size
    private val cols = grid[0].size

    private val tops = mutableSetOf<HikingPosition>()
    private var rating = 0

    fun getDistinctPositionsCount(): Int {
        var totalTops = 0
        findStarts().forEach {
            tops.clear()
            walkTillTheEnd(it)
            totalTops += tops.size
        }
        return totalTops
    }

    fun getRating(): Int {
        var totalRating = 0
        findStarts().forEach {
            rating = 0
            walkTillTheEnd(it)
            totalRating += rating
        }
        return totalRating
    }

    private fun walkTillTheEnd(currentPosition: HikingPosition) {
        if (currentPosition.isOutOfBounds()) return

        if (currentPosition.isEnd()) {
            tops.add(currentPosition)
            rating++
            return
        }

        currentPosition.getNeighbours()
            .filter { !it.isOutOfBounds() && it.getVal() == currentPosition.getVal() + 1 }
            .forEach { walkTillTheEnd(it) }
    }

    private fun findStarts(): List<HikingPosition> {
        val starts = mutableListOf<HikingPosition>()
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                if (grid[row][col] == '0') {
                    starts.add(HikingPosition(row, col, grid))
                }
            }
        }
        return starts
    }

    private data class HikingPosition(val row: Int, val col: Int, val grid: Array<Array<Char>>) {
        private val rows = grid.size
        private val cols = grid[0].size

        fun isOutOfBounds() = row < 0 || row >= rows || col < 0 || col >= cols
        fun getVal() = grid[row][col]
        fun isEnd() = grid[row][col] == '9'
        fun getNeighbours() = listOf(
            HikingPosition(row - 1, col, grid),
            HikingPosition(row + 1, col, grid),
            HikingPosition(row, col - 1, grid),
            HikingPosition(row, col + 1, grid)
        )
    }


}



