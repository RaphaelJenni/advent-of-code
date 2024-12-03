fun main() {
    val testDay03 = Day03("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")
    check(testDay03.getSum() == 161L) { "Found ${testDay03.getSum()}" }
    val testDay03P2 = Day03("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")
    check(testDay03P2.getSumOfActivatedMuls() == 48L) { "Found ${testDay03P2.getSumOfActivatedMuls()}" }

    val input = readInputAsOne("data/day03/actual.day03")
    val actualDay03 = Day03(input)
    actualDay03.getSum().println()
    actualDay03.getSumOfActivatedMuls().println()
}

class Day03(val input: String) {
    fun getSum(): Long {
        return FIND_PATTERN.findAll(input)
//            .onEach { it.value.println() }
            .map { it.groups[1]!!.value.toLong() * it.groups[2]!!.value.toLong() }.sum()
    }

    fun getSumOfActivatedMuls(): Long {
        var summingActive = true
        return FIND_PATTERN_WITH_CONTROLS.findAll(input)
//            .onEach { it.value.println() }
            .filter {
                if (it.value == "do()") {
                    summingActive = true
                    return@filter false
                } else if (it.value == "don't()") {
                    summingActive = false
                    return@filter false
                }
                return@filter true
            }
            .filter { summingActive }
            .map { it.groups[2]!!.value.toLong() * it.groups[3]!!.value.toLong() }.sum()
    }

    companion object {
        val FIND_PATTERN = Regex("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)")
        val FIND_PATTERN_WITH_CONTROLS = Regex("(mul\\(([0-9]{1,3}),([0-9]{1,3})\\))|do\\(\\)|don't\\(\\)")
    }

}