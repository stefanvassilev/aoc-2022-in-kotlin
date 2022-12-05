import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.set

fun main() {
    val input = readInput("Day05")
    day5Part1(input)
    day5Part2(input)

}


fun day5Part1(input: List<String>): Int {
    val (stacks, commands) = parseStacksAndCommands(input)

    commands.forEach {
        val (crateFrom, crateTo, crateCount) = parseCommand(it)

        val temp = ArrayList<Char>()
        for (i in 0 until crateCount) {
            temp.add(stacks[crateFrom].removeFirst())
        }
        temp.forEach { el ->
            stacks[crateTo].addFirst(el)
        }
    }

    stacks.forEach { print(it.first()) }
    println()
    return 0
}


fun day5Part2(input: List<String>): Int {
    val (stacks, commands) = parseStacksAndCommands(input)

    commands.forEach {
        val (crateFrom, crateTo, crateCount) = parseCommand(it)

        val temp = ArrayList<Char>()
        for (i in 0 until crateCount) {
            temp.add(stacks[crateFrom].removeFirst())
        }
        stacks[crateTo].addAll(0, temp)
    }

    stacks.forEach { print(it.first()) }
    println()
    return 0
}

private fun parseStacksAndCommands(input: List<String>): Pair<ArrayList<ArrayDeque<Char>>, List<String>> {
    val stacks = ArrayList<ArrayDeque<Char>>()

    val indexOfInputSplit = input.indexOf("")
    var stacksInput = input.subList(0, indexOfInputSplit)
    val commands = input.subList(indexOfInputSplit + 1, input.size)

    val stacksCount = getStacksCount(stacksInput)
    stacksInput = stacksInput.subList(0, stacksInput.size - 1)
    initStacks(stacksCount, stacks)

    populateStacks(stacksCount, input, indexOfInputSplit, stacksInput, stacks)
    return Pair(stacks, commands)
}


private fun initStacks(stacksCount: Int, stacks: ArrayList<ArrayDeque<Char>>) {
    for (i in 0 until stacksCount) {
        stacks.add(ArrayDeque())
    }
}

private fun getStacksCount(stacksInput: List<String>) =
        stacksInput.last().split(" ").filter { it.isNotBlank() }.last().toInt()

private fun populateStacks(stacksCount: Int, input: List<String>, indexOfInputSplit: Int, stacksInput: List<String>, stacks: ArrayList<ArrayDeque<Char>>) {
    val indexMap = TreeMap<Int, Int>()
    for (i in 1..stacksCount) {
        indexMap[i] = input[indexOfInputSplit - 1].indexOf(i.toString())
    }

    for (i in stacksInput.size - 1 downTo 0) {
        var lineStack = stacksInput[i]
        while (lineStack.length <= indexMap.get(stacksCount)!!) {
            lineStack += " "
        }

        for ((k, j) in (1..stacksCount).withIndex()) {
            val element = lineStack[indexMap[j]!!]
            if (element != ' ') {
                stacks[k].addFirst(element)
            }
        }

    }
}

private fun parseCommand(it: String): Triple<Int, Int, Int> {
    val crateFrom = (it.substringAfter("from ").substringBefore(" to")).toInt() - 1
    val crateTo = (it.substringAfter("to ")).toInt() - 1
    val crateCount = it.substringAfter("move ").substringBefore(" from").toInt()
    return Triple(crateFrom, crateTo, crateCount)
}



