import java.util.*

fun main() {
    val testInput = readInputAsOne("data/day09/sample.day09")
    val test = FileCompacter(testInput)
    val checksum1 = test.checksum()
    check(checksum1 == 60L) { "checksum - Found $checksum1" }

    println()

    val testInput2 = readInputAsOne("data/day09/sample2.day09")
    val test2 = FileCompacter(testInput2)
    val checksum2 = test2.checksum()
    check(checksum2 == 1928L) { "checksum - Found $checksum2" }
    val checksumWithBlocks2 = test2.checksumWithBlocks()
    check(checksumWithBlocks2 == 2858L) { "checksumWithBlocks - Found $checksumWithBlocks2" }

    println()

    val input = readInputAsOne("data/day09/actual.day09")
    val actual = FileCompacter(input)
    actual.checksum().println()

    val actual2 = FileCompacter(input)
    actual2.checksumWithBlocks().println()

}

class FileCompacter(
    private val input: String,
) {
    private fun defragment(): List<Int> {
        val expandedList = expandInput(input)

        var left = 0
        var right = expandedList.size - 1
        val result = LinkedList<Int>()

        while (left < right) {
            if (expandedList[left] != SPACE) {
                result.add(expandedList[left])
            } else {
                while (expandedList[right] == SPACE) {
                    right--
                }
                result.add(expandedList[right])
                right--
            }
            left++
        }

        if (expandedList[left] != SPACE) {
            result.add(expandedList[left])
        }

//        println(result.joinToString(""))

        return result
    }


    /**
     * This time, attempt to move whole files to the leftmost span of free space blocks that could fit the file. Attempt to move each file exactly once in order of decreasing file ID number starting with the file with the highest file ID number. If there is no span of free space to the left of a file that is large enough to fit the file, the file does not move.
     *
     * The first example from above now proceeds differently:
     *
     * 00...111...2...333.44.5555.6666.777.888899
     * 0099.111...2...333.44.5555.6666.777.8888..
     * 0099.1117772...333.44.5555.6666.....8888..
     * 0099.111777244.333....5555.6666.....8888..
     * 00992111777.44.333....5555.6666.....8888..
     * The process of updating the filesystem checksum is the same; now, this example's checksum would be 2858.
     */
    private fun defragmentWholeBlocks(): LinkedList<Block> {
        val expandedList = expandInputAsBlocks(input)

//        println(expandedList.joinToString(""))

        var left = 0
        var lastUnusedBlock = expandedList.size - 1
        val result = LinkedList<Block>()

        while (left < lastUnusedBlock) {
            if (expandedList[left].id != SPACE) {
                result.add(expandedList[left])
                left++
            } else {
                while (expandedList[lastUnusedBlock].id == SPACE) {
                    lastUnusedBlock--
                }

                var right = lastUnusedBlock
                while (right > left && (expandedList[right].length > expandedList[left].length || expandedList[right].id == SPACE)) {
                    right--
                }

                if (right <= left) {
                    result.add(expandedList[left])
                    left++
                    continue
                }

                result.add(expandedList[right])

                if (expandedList[left].length > expandedList[right].length) {
                    expandedList[left].length -= expandedList[right].length
                } else {
                    left++
                }

                expandedList[right] = Block(SPACE, expandedList[right].length)

                if (right == lastUnusedBlock) {
                    lastUnusedBlock--
                }
            }
        }

        if (expandedList[left].id != SPACE) {
            result.add(expandedList[left])
        }


//        println(result.joinToString(""))

        return result
    }

    /**
     * The final step of this file-compacting process is to update the filesystem checksum. To calculate the checksum, add up the result of multiplying each of these blocks' position with the file ID number it contains. The leftmost block is in position 0. If a block contains free space, skip it instead.
     */
    fun checksum(): Long {
        val result = defragment()
        var sum = 0L
        for ((index, item) in result.withIndex()) {
            if (item != SPACE) {
                sum += index * item
            }
        }

        return sum
    }

    fun checksumWithBlocks(): Long {
        var index = 0
        val result = defragmentWholeBlocks()
        var sum = 0L
        for (item in result) {
            if (item.id != SPACE) {
                for (i in 0 until item.length) {
                    sum += index * item.id
                    index++
                }
            } else {
                index += item.length
            }
        }

        return sum
    }

    companion object {
        private const val SPACE = -1

        private fun expandInput(input: String): List<Int> {
            val inputArray = input.toCharArray()
            val expandedList = LinkedList<Int>()
            var id = 0
            var index = 0

            while (index < inputArray.size) {
                val fileLength = inputArray[index].digitToInt()
                for (i in 0 until fileLength) {
                    expandedList.add(id)
                }

                if (index + 1 >= inputArray.size) break
                val spaceLength = inputArray[index + 1].digitToInt()

                for (i in 0 until spaceLength) {
                    expandedList.add(SPACE)
                }
                id++
                index += 2
            }

//            println(expandedList.joinToString(""))

            return expandedList.toList()
        }

        private fun expandInputAsBlocks(input: String): MutableList<Block> {
            val inputArray = input.toCharArray()
            val expandedList = LinkedList<Block>()
            var id = 0
            var index = 0

            while (index < inputArray.size) {
                val fileLength = inputArray[index].digitToInt()
                expandedList.add(Block(id, fileLength))

                if (index + 1 >= inputArray.size) break
                val spaceLength = inputArray[index + 1].digitToInt()

                expandedList.add(Block(SPACE, spaceLength))

                id++
                index += 2
            }

            return expandedList.toMutableList()
        }

    }

    data class Block(val id: Int, var length: Int) {
        override fun toString(): String {
            return if (id == SPACE) {
                ".".repeat(length)
            } else {
                id.toString().repeat(length)
            }
        }
    }
}
