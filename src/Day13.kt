import java.util.*

fun main() {
    val input = readInput("Day13")
    println(day13Part1(input))
    println(day13Part2(input))

}


interface ListElem

data class EmbeddedList(val list: ArrayList<ListElem> = ArrayList()) : ListElem {
    override fun toString(): String {
        return list.toString()
    }
}

data class IntHolder(val int: Int) : ListElem {
    override fun toString(): String {
        return int.toString()
    }
}


fun day13Part1(input: List<String>): Int {
    val pairs = getPairsFromInput(input)

    var sumOfIndexes = 0
    var curPairIndex = 1
    for (pair in pairs) {
        val el1 = getEmbeddedList(pair.first)
        val el2 = getEmbeddedList(pair.second)

        if (pairInRightOrder(el1.list[0], el2.list[0]) > 0) {
            sumOfIndexes += curPairIndex
        }
        curPairIndex++
    }

    return sumOfIndexes
}

fun day13Part2(input: List<String>): Int {
    val pairs = getPairsFromInput(input)
    val packets = pairs
            .map { listOf(getEmbeddedList(it.first), getEmbeddedList(it.second)) }
            .flatten().toMutableList()
    packets.add(getEmbeddedList("[[2]]"))
    packets.add(getEmbeddedList("[[6]]"))


    var curPacket = 1
    var dividerPacketIndex1 = -1
    var dividerPacketIndex2 = -1
    packets.sortedWith { o1, o2 -> pairInRightOrder(o1, o2) }
            .reversed()
            .forEach {
                if (it.toString() == "[[[2]]]") {
                    dividerPacketIndex1 = curPacket
                }
                if (it.toString() == "[[[6]]]") {
                    dividerPacketIndex2 = curPacket
                }
                curPacket++
            }

    println("Index of divider packet 1: $dividerPacketIndex1")
    println("Index of divider packet 2: $dividerPacketIndex2")

    return dividerPacketIndex1 * dividerPacketIndex2
}

private fun getPairsFromInput(input: List<String>): ArrayList<Pair<String, String>> {
    val pairs = ArrayList<Pair<String, String>>()
    val tmp = ArrayList<String>()

    for (inp in input) {
        if (inp == "") {
            pairs.add(Pair(tmp[0], tmp[1]))
            tmp.clear()
            continue
        }
        tmp.add(inp)
    }
    pairs.add(Pair(tmp[0], tmp[1]))
    return pairs
}

private fun getEmbeddedList(str: String): EmbeddedList {
    val el1 = EmbeddedList()
    getListElem(el1, str)
    return el1
}

fun pairInRightOrder(data1: ListElem, data2: ListElem): Int {
    if (data1 is IntHolder && data2 is IntHolder) {
        return data2.int - data1.int
    }
    if (data1 is IntHolder && data2 is EmbeddedList) {
        return pairInRightOrder(EmbeddedList(ArrayList(listOf(data1))), data2)
    }
    if (data2 is IntHolder && data1 is EmbeddedList) {
        return pairInRightOrder(data1, EmbeddedList(ArrayList(listOf(data2))))
    }

    val el1 = data1 as EmbeddedList
    val el2 = data2 as EmbeddedList
    val el1Iter = el1.list.iterator()
    val el2Iter = el2.list.iterator()

    while (el1Iter.hasNext()) {
        if (el1Iter.hasNext() && !el2Iter.hasNext()) {
            return -1
        }

        val item1 = el1Iter.next()
        val item2 = el2Iter.next()
        val comparison = pairInRightOrder(item1, item2)
        if (comparison != 0) {
            return comparison
        }
    }

    return if (el1.list.size == el2.list.size) 0 else 1
}

fun getListElem(root: EmbeddedList, inputStr: String) {
    var inp = inputStr
    if (inp == "") {
        return
    }
    if (inp.all { it.isDigit() }) {
        root.list.add(IntHolder(Integer.parseInt(inp)))
        return
    }

    if (!inp.contains("[") && !inp.contains("]")) {
        root.list.addAll(inp.split(",").map { IntHolder(it.toInt()) })
    }

    if (!inp.startsWith("[") && inp.contains("[")) {
        root.list.addAll(inp.substringBefore(",[").split(",").map { IntHolder(it.toInt()) })
        inp = inp.substring(inp.indexOf('['), inp.length)
    }

    if (inp.startsWith("[")) {
        val embedded = EmbeddedList()
        val closingBracketIndex = getClosingBracketIndex(inp)

        val embeddedSubList = inp.substring(1, closingBracketIndex)
        getListElem(embedded, embeddedSubList)
        root.list.add(embedded)
        getListElem(root, inp.substring(closingBracketIndex, inp.length).substringAfter(',', missingDelimiterValue = ""))
        return
    }
}

private fun getClosingBracketIndex(inp: String): Int {
    val closingBracketIndex: Int
    val heap: Deque<Char> = LinkedList()
    heap.add('[')


    val iter = inp.iterator()
    var index = 1
    iter.next() // skip 1st element
    while (heap.isNotEmpty()) {
        val next = iter.next()
        if (next == '[') {
            heap.add('[')
        }
        if (next == ']') {
            heap.pop()
        }
        index++
    }
    closingBracketIndex = index - 1
    return closingBracketIndex
}


