import kotlin.math.abs

const val SEPARATOR = "   "


fun main() {
    val testInput = readInput("data/day01/sample.day01")
    val testDay01 = Day01(testInput)
    check(testDay01.findDistance() == 11)
    check(testDay01.similarityScore() == 31)

    val input = readInput("data/day01/actual.day01")
    val actualDay01 = Day01(input)
    actualDay01.findDistance().println()
    actualDay01.similarityScore().println()
}


class Day01(
    input: List<String>
) {
    private val left = mutableListOf<Int>()
    private val right = mutableListOf<Int>()

    init {
        input.forEach {
            val value = it.split(SEPARATOR)
            left.add(value[0].toInt())
            right.add(value[1].toInt())
        }

        left.sort()
        right.sort()
    }

    fun findDistance(): Int {
        var distance = 0
        for (i in left.indices) {
            distance += abs(right[i] - left[i])
        }
        return distance
    }

    fun similarityScore() : Int {
        var score = 0
        for (i in left.indices) {
            val leftValue = left[i]
            val rightValue = right.count { it == leftValue }
            score += leftValue * rightValue
        }
        return score
    }
}
