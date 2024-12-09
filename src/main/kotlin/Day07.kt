fun main() {
    val testInput = readInput("data/day07/sample.day07")
    val test = EquationsVerifier(testInput)

    val sumOfValidInputs = test.getSumOfValidInputs()
//    check(sumOfValidInputs == 3749L) { "getSumOfValidInputs - Found $sumOfValidInputs" }
    check(sumOfValidInputs == 11387L) { "getSumOfValidInputs2 - Found $sumOfValidInputs" }

    val input = readInput("data/day07/actual.day07")
    val actual = EquationsVerifier(input)
    actual.getSumOfValidInputs().println()
}

private class EquationsVerifier(
    private val input: List<String>,
) {
    fun getSumOfValidInputs(): Long {
        return input
            .map { EquationVerifier(it) }
            .filter { it.isAValidEquation() }
            .sumOf { it.targetValue }
    }
}

private class EquationVerifier(
    testInput: String,
) {
    val targetValue: Long = testInput.split(":")[0].toLong()
    private val numbers: List<Long> = testInput.split(": ")[1].split(" ").map { it.toLong() }

    fun isAValidEquation(): Boolean {
        return getPermutations(numbers.size - 1).asSequence()
            .map { processNumbers(it) }
            .any {it == targetValue }
    }

    fun processNumbers(operations: List<(a: Long, b: Long) -> Long>): Long {
        var total = numbers[0]
        for ((i, op) in operations.withIndex()) {
            total = op(total, numbers[i + 1])
        }
        return total
    }

    fun getPermutations(length: Int): List<List<(a: Long, b: Long) -> Long>> {
//        if (length == 1) return listOf(listOf(ADD), listOf(MUL)) // PART 1
        if (length == 1) return listOf(listOf(ADD), listOf(MUL), listOf(CONCAT))

        val permutations = getPermutations(length - 1)
        val withAdd = permutations.map { subPermutations -> listOf(ADD).plus(subPermutations) }
        val withMul = permutations.map { subPermutations -> listOf(MUL).plus(subPermutations) }
        val withConcat = permutations.map { subPermutations -> listOf(CONCAT).plus(subPermutations) } // PART 2

//        return withAdd.plus(withMul) // PART 1
        return withAdd.plus(withMul).plus(withConcat)
    }

    companion object {
        val ADD = { a: Long, b: Long -> a + b }
        val MUL = { a: Long, b: Long -> a * b }
        val CONCAT = { a: Long, b: Long -> (a.toString() + b.toString()).toLong() }
    }
}