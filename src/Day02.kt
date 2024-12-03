import kotlin.math.abs

fun main() {
    val testInput = readInput("data/day02/sample.day02")
    val testDay02 = Day02(testInput)
    check(testDay02.numberOfSafeReports() == 2) { "Found ${testDay02.numberOfSafeReports()}" }
    check(testDay02.numberOfSafeReportsWithTolerance() == 5) { "Found ${testDay02.numberOfSafeReportsWithTolerance()}" }

    val input = readInput("data/day02/actual.day02")
    val actualDay02 = Day02(input)
    actualDay02.numberOfSafeReports().println()
    actualDay02.numberOfSafeReportsWithTolerance().println()
}


private class Day02(
    input: List<String>,
) {
    private val reports: List<Report> = input.map {
        Report(it.split(" ").map(String::toInt))
    }

    fun numberOfSafeReports(): Int {
        return reports.count {
            it.isSafe()
        }
    }

    fun numberOfSafeReportsWithTolerance(): Int {
        return reports.count {
//            println("${it.levels} with tolerance is safe? ${it.isSafeWithErrorTolerance()}")
            it.isSafeWithErrorTolerance()
        }
    }
}

private class Report(
    val levels: List<Int>,
) {
    fun isSafe(): Boolean {
        return levels.isMonotonicAndRespectsLimit()
    }

    fun isSafeWithErrorTolerance(): Boolean {
        if (isSafe()) return true

        // brute force approach
        return levels.indices.any { index ->
            // remove one element at the time
            levels.filterIndexed { i, _ -> i != index }.isMonotonicAndRespectsLimit()
        }
    }


    private fun List<Int>.isMonotonicAndRespectsLimit(): Boolean {
        val levelPairs = windowed(size = 2, step = 1, partialWindows = false)
        val checkLimitRespect = { a: Int, b: Int -> abs(a - b) in 1..3 }
        return levelPairs.all { (a, b) -> a > b && checkLimitRespect(a, b) } ||
            levelPairs.all { (a, b) -> a < b && checkLimitRespect(a, b) }
    }
}
