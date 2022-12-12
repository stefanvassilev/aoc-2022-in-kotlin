import java.math.BigDecimal

fun main() {
    val input = readInput("Day11")
    println(day11Part1(input))
    println(day11Part2(input))

}

data class PersonalItem(var worryLevel: Long)

data class Monkey(
        val number: Int,
        var startingItems: MutableList<PersonalItem>,
        val worryOperation: WorryOperation,
        val testOp: TestOperation,
        val testSuccessMonkeyNum: Int,
        val testFailMonkeyNum: Int,
) {
    var inspectedItems = 0

    fun completeRound(monkeys: List<Monkey>, worryReliefLevel: Int) {
        val itemsToRemove = HashSet<PersonalItem>()
        for (item in startingItems) {
            itemsToRemove.add(item)
            inspectedItems++


            var itemWorryLevel = worryOperation.evaluate(item.worryLevel)
            if (worryReliefLevel == 3) {
                itemWorryLevel /= worryReliefLevel
            } else {
                itemWorryLevel %= 9699690
            }
            val monkeyIndex: Int = if (testOp.evaluate(itemWorryLevel)) {
                testSuccessMonkeyNum
            } else {
                testFailMonkeyNum
            }

            monkeys.first { it.number == monkeyIndex }.startingItems.add(PersonalItem(itemWorryLevel))
        }

        startingItems.removeAll(itemsToRemove)
    }

}

interface WorryOperation {
    fun evaluate(old: Long): Long
}

interface TestOperation {
    fun evaluate(worryLevel: Long): Boolean
}

class DivisibleTestOperation(private val dividableBy: Long) : TestOperation {
    override fun evaluate(worryLevel: Long): Boolean {
        return worryLevel % dividableBy == 0L
    }

}

class MultiplicationWorryOp(private val multiplyBy: String) : WorryOperation {
    override fun evaluate(old: Long): Long {
        return if (multiplyBy == "old") {
            old * old
        } else {
            old * multiplyBy.toLong()
        }
    }

}

class AdditionWorryOp(private val summand: String) : WorryOperation {
    override fun evaluate(old: Long): Long {
        return if (summand == "old") {
            old + old
        } else {
            old + summand.toLong()
        }
    }

}


fun day11Part1(input: List<String>): Int {
    val monkeys = getMonkeysFromInput(input)
    for (i in 1..20) {
        monkeys.forEach {
            it.completeRound(monkeys, 3)
        }
        println("round $i complete")
    }
    val inspected = monkeys.map { it.inspectedItems }.sorted()
    println(inspected)
    val monkeyBusiness = inspected[inspected.size - 1] * inspected[inspected.size - 2]
    println("monkey business $monkeyBusiness")

    return monkeyBusiness
}

fun day11Part2(input: List<String>): Int {
    val monkeys = getMonkeysFromInput(input)
    for (i in 1..10000) {
        monkeys.forEach {
            it.completeRound(monkeys, 1)
        }
    }

    val inspected = monkeys.map { it.inspectedItems }.sorted()
    println(inspected)
    val monkeyBusiness = BigDecimal(inspected[inspected.size - 1]).multiply(BigDecimal(inspected[inspected.size - 2]))
    println("monkey business $monkeyBusiness")

    return monkeyBusiness.toInt()
}

private fun getMonkeysFromInput(input: List<String>): ArrayList<Monkey> {
    val monkeyRawInput = ArrayList<ArrayList<String>>()
    val curMonkey = ArrayList<String>()
    for (line in input) {
        curMonkey.add(line)
        if (line == "") {
            monkeyRawInput.add(ArrayList(curMonkey))
            curMonkey.clear()
        }
    }
    monkeyRawInput.add(ArrayList(curMonkey))
    val monkeys = ArrayList<Monkey>()
    for (line in monkeyRawInput) {
        monkeys.add(parseMonkey(line))
    }
    return monkeys
}

private fun parseMonkey(line: ArrayList<String>): Monkey {
    val index = line[0].substringAfter("Monkey ").substringBefore(":").toInt()
    val staringItems = line[1].substringAfter("Starting items: ").split(",").map { it.trim() }.map { it.toInt() }.map { PersonalItem(it.toLong()) }.toMutableList()

    val worryOp = when {
        line[2].contains("*") -> MultiplicationWorryOp(line[2].substringAfter("* "))
        line[2].contains("+") -> AdditionWorryOp(line[2].substringAfter("+ "))
        else -> {
            throw IllegalArgumentException("unknown worry op")
        }
    }
    val testOp = DivisibleTestOperation(line[3].substringAfter("divisible by ").toLong())
    val testOpSuccessMonkeyIndex = line[4].substringAfter("throw to monkey ").toInt()
    val testOpFailMonkeyIndex = line[5].substringAfter("throw to monkey ").toInt()
    return Monkey(
            index,
            staringItems,
            worryOp,
            testOp,
            testOpSuccessMonkeyIndex,
            testOpFailMonkeyIndex
    )
}

