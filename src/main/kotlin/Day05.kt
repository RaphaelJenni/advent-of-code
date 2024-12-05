fun main() {
    val testInput = readInput("data/day05/sample.day05")
    val test = PageOrdering(testInput)
    check(test.getSumOfCenterOfCorrectlyOrdered() == 143) { "getSumOfCenterOfCorrectlyOrdered - Found ${test.getSumOfCenterOfCorrectlyOrdered()}" }
    check(test.getSumOfCenterOfFixed() == 123) { "getSumOfCenterOfCorrectlyOrdered - Found ${test.getSumOfCenterOfFixed()}" }

    val input = readInput("data/day05/actual.day05")
    val actual = PageOrdering(input)
    actual.getSumOfCenterOfCorrectlyOrdered().println()
    actual.getSumOfCenterOfFixed().println()
}

private class PageOrdering(input: List<String>) {
    private val rules: MutableMap<Int, MutableSet<Int>> = mutableMapOf()
    private val updates: MutableList<List<Int>> = mutableListOf()

    init {
        var firstPart = true
        input.forEach {
            if (it.isEmpty()) {
                firstPart = false
            } else {
                if (firstPart) {
                    val (part1, part2) = it.split("|")
                    rules.computeIfAbsent(part1.toInt()) { mutableSetOf() }.add(part2.toInt())
                } else {
                    updates.add(it.split(",").map { value -> value.toInt() })
                }
            }
        }
    }

    fun getSumOfCenterOfCorrectlyOrdered(): Int {
        return findCorrectlyOrdered().sumOf { it[it.size / 2] }
    }

    fun getSumOfCenterOfFixed(): Int {
        return fixIncorrectlyOrdered().sumOf { it[it.size / 2] }
    }

    fun findCorrectlyOrdered(): List<List<Int>> {
        return updates.filter { it.isCorrectlyOrdered() }
//            .onEach { it.println() }
    }

    fun fixIncorrectlyOrdered(): List<List<Int>> {
        return updates
            .filter { !it.isCorrectlyOrdered() }
            .map { it.fixOrder() }
    }

    fun List<Int>.isCorrectlyOrdered(): Boolean {
        // brute force approach
        for ((index, before) in this.withIndex()) {
            val mustBeAfters = rules[before]
            for ((checkIndex, check) in this.withIndex()) {
                if (index == checkIndex) break
                if (checkIndex < index && mustBeAfters?.contains(check) == true) return false
            }
        }
        return true
    }

    private fun List<Int>.fixOrder() = sortedWith { a, b ->
        when {
            rules[a]?.contains(b) == true -> -1
            rules[b]?.contains(a) == true -> 1
            else -> 0
        }
    }
}
