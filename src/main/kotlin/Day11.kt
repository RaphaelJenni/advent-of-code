fun main() {
    val testInput = readInputAsOne("data/day11/sample.day11")
    val test = StoneArrangement(testInput)
    val blink1 = test.blinkToString()
    check(blink1 == "253000 1 7") { "Blink 1: $blink1" }
    val blink2 = test.blinkToString()
    check(blink2 == "253 0 2024 14168") { "Blink 2: $blink2" }
    val blink3 = test.blinkToString()
    check(blink3 == "512072 1 20 24 28676032") { "Blink 3: $blink3" }
    val blink4 = test.blinkToString()
    check(blink4 == "512 72 2024 2 0 2 4 2867 6032") { "Blink 4: $blink4" }
    val blink5 = test.blinkToString()
    check(blink5 == "1036288 7 2 20 24 4048 1 4048 8096 28 67 60 32") { "Blink 5: $blink5" }
    val blink6 = test.blinkToString()
    check(blink6 == "2097446912 14168 4048 2 0 2 4 40 48 2024 40 48 80 96 2 8 6 7 6 0 3 2") { "Blink 6: $blink6" }

    val test2 = StoneArrangementIndividualStones(testInput)
    val blinkTimesAndGetStones1 = test2.blinkTimesAndGetNumberOfStones(6)
    check(blinkTimesAndGetStones1 == 22L) { "Test 2 after 6 steps expect 22 Stones, got: $blinkTimesAndGetStones1" }

    val test3 = StoneArrangementIndividualStones(testInput)
    val blinkTimesAndGetStones2 = test3.blinkTimesAndGetNumberOfStones(25)
    check(blinkTimesAndGetStones2 == 55312L) { "Test 3 after 25 steps expect 55312 Stones, got: $blinkTimesAndGetStones2" }


    println("ACTUAL")
    val actualInput = readInputAsOne("data/day11/actual.day11")
    val actual = StoneArrangementIndividualStones(actualInput)
    actual.blinkTimesAndGetNumberOfStones(25).println()

    val actual2 = StoneArrangementIndividualStones(actualInput)
    actual2.blinkTimesAndGetNumberOfStones(75).println()

}

class StoneArrangementIndividualStones(val input: String) {
    val arrangement = input.split(" ").map { it.toLong() }
    val mem = mutableMapOf<Pair<Long, Int>, Long>()

    fun blinkTimesAndGetNumberOfStones(n: Int): Long {
        return arrangement.sumOf { stone -> stone.numberOfStonesAfterBlinks(n) }
    }

    fun Long.numberOfStonesAfterBlinks(numberOfBlinks: Int): Long {
        if (numberOfBlinks == 0) return 1
        return mem.getOrPut(this to numberOfBlinks) {
            this.blink().sumOf { subStone -> subStone.numberOfStonesAfterBlinks(numberOfBlinks - 1) }
        }
    }

    private fun Long.blink(): List<Long> = when {
        this == 0L -> listOf(1L)
        toString().length.isEven() -> {
            val numText = toString()
            val half = numText.length / 2
            listOf(numText.take(half).toLong(), numText.takeLast(half).toLong())
        }

        else -> listOf(this * 2024L)
    }

    private fun Int.isEven(): Boolean = (this % 2) == 0

}

class StoneArrangement(val input: String) {

    val arrangement: MyLinkedList<String> = MyLinkedList<String>(input.split(' ').map(String::trim))


    fun blinkToString(): String {
        blink()

        println(arrangement.joinToString(" "))

        return arrangement.joinToString(" ")
    }

    fun blink(): MyLinkedList<String> {
        var node: MyLinkedList.Node<String>? = arrangement.firstNode()

        while (node != null) {
            when {
                node.value == "0" -> node.value = "1"
                node.value.length.isEven() -> {
                    val value = node.value
                    val stone1 = node.value.substring(0 until value.length / 2).toLong().toString()
                    val stone2 = node.value.substring(value.length / 2 until value.length).toLong().toString()
                    node.value = stone2
                    arrangement.linkBeforeNode(stone1, node)
                }

                else -> node.value = (node.value.toLong() * 2024L).toString()
            }
            node = node.next
        }
        return arrangement
    }

    fun blinkTimesAndGetStones(n: Int): Long {
        for (i in 0 until n) {
            blink()
        }
        return arrangement.length()
    }

    fun Int.isEven(): Boolean = (this % 2) == 0


}

class MyLinkedList<T> {
    var head: Node<T>? = null
        private set

    class Node<T>(var value: T, var next: Node<T>? = null)

    constructor()

    constructor(other: List<T>) {
        if (other.isNotEmpty()) {
            head = Node(other[0])
            var current = head
            for (i in 1 until other.size) {
                val newNode = Node(other[i])
                current?.next = newNode
                current = newNode
            }
        }
    }

    fun firstNode(): Node<T>? {
        return head
    }

    fun linkBeforeNode(newValue: T, beforeNode: Node<T>?) {
        if (beforeNode == null) {
            // If beforeNode is null, we add the new node at the end of the list
            if (head == null) {
                head = Node(newValue)
            } else {
                var current = head
                while (current?.next != null) {
                    current = current.next
                }
                current?.next = Node(newValue)
            }
        } else {
            // If beforeNode is the head, we need to update the head
            if (head == beforeNode) {
                val newNode = Node(newValue, head)
                head = newNode
            } else {
                // Otherwise, we find the node before the beforeNode and insert the new node
                var current = head
                while (current?.next != beforeNode) {
                    current = current?.next
                }
                if (current != null) {
                    val newNode = Node(newValue, beforeNode)
                    current.next = newNode
                }
            }
        }
    }

    override fun toString(): String {
        val result = StringBuilder()
        var current = head
        while (current != null) {
            result.append(current.value).append(" -> ")
            current = current.next
        }
        result.append("null")
        return result.toString()
    }

    fun joinToString(separator: String = ", ", prefix: String = "", postfix: String = ""): String {
        val result = StringBuilder(prefix)
        var current = head
        while (current != null) {
            result.append(current.value)
            if (current.next != null) {
                result.append(separator)
            }
            current = current.next
        }
        result.append(postfix)
        return result.toString()
    }

    fun length(): Long {
        var count = 0L
        var current = head
        while (current != null) {
            count++
            current = current.next
        }
        return count
    }
}


