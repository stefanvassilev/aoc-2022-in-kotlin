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

    var sumOfIndexes = 0
    var curPairIndex = 1
    for (pair in pairs) {
        val el1 = getEmbeddedList(pair.first)
        val el2 = getEmbeddedList(pair.second)

        println("Input:\nData1: ${el1.list[0]}\nData2: ${el2.list[0]}")
        if (pairInRightOrder(el1.list[0], el2.list[0]) > 0) {
            println("correct order for pair: $curPairIndex")
            sumOfIndexes += curPairIndex
        } else {
            println("NOT correct order for pair: $curPairIndex")
        }
        curPairIndex++
    }

    return sumOfIndexes
}

private fun getEmbeddedList(str: String): EmbeddedList {
    val el1 = EmbeddedList()
    getListElem(el1, str)
    return el1
}

fun pairInRightOrder(data1: ListElem, data2: ListElem): Int {
    println("*** :\nData1: ${data1}\nData2: $data2")
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


fun day13Part2(input: List<String>): Int {
    return 0
}

//That's not the right answer; your answer is too low. If you're stuck, make sure you're using the full input data; there are also some general tips on the about page, or you can ask for hints on the subreddit. Please wait one minute before trying again. (You guessed 1197.) [Return to Day 13]