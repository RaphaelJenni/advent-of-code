import kotlin.math.abs

fun main() {
    val testInput = readInput("data/day02/sample.day02")
    val testDay02 = Day02(testInput)
    check(testDay02.numberOfSafeReports() == 2) { "Found ${testDay02.numberOfSafeReports()}" }
    check(testDay02.numberOfSafeReportsWithTolerance() == 4)

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
        if (levels.size <= 1) return true

        if (levels[0] == levels[1]) return false

        val isDescending = levels[0] > levels[1]

        for (i in 1 until levels.size) {
            val delta = abs(levels[i] - levels[i - 1])
            if(delta !in 1..3) return false
            if (isDescending) {
                if (levels[i] > levels[i - 1]) return false
            } else {
                if (levels[i] < levels[i - 1]) return false
            }
        }
        return true
    }

    fun isSafeWithErrorTolerance(): Boolean {
        if (levels.size <= 1) return true

        if (levels[0] == levels[1]) return false

        val isDescending = levels[0] > levels[1]

        var errors = 0

        val correctedLevels = mutableListOf(levels[0])

        for (i in 1 until levels.size) {
            val last = correctedLevels.last()
            val delta = abs(levels[i] - last)

            val isError = (delta !in 1..3) || if (isDescending) levels[i] > last else levels[i] < last

            if (isError) {
                errors++
            } else {
                correctedLevels.add(levels[i])
            }

            if (errors > 1) return false
        }
        return true
    }
}
