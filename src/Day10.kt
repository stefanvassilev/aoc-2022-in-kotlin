fun main() {
    val input = readInput("Day10")
    println(day10Part1(input))
    println(day10Part2(input))

}


fun day10Part1(input: List<String>): Int {
    val commands = input.map { it: String ->
        if (it.contains("addx")) {
            AddOp(it.substringAfter("addx ").toInt())
        } else {
            NoOp()
        }
    }.toMutableList()
    var xRegister = 1
    var signalStrength = 0


    val delayedComputations = HashMap<Int, ArrayList<Operation>>()
    var clock = 0
    while (commands.isNotEmpty()) {
        val op = commands.first()
        commands.removeAt(0)
        addCommand(delayedComputations, op, clock)
        clock += op.getExecutionTime()
    }


    val neededSignals = HashMap<Int, Int>()
    neededSignals[20] = 0
    neededSignals[60] = 0
    neededSignals[100] = 0
    neededSignals[140] = 0
    neededSignals[180] = 0
    neededSignals[220] = 0

    for (i in 0..clock) {
        val operations = delayedComputations[0] ?: ArrayList()
        if (delayedComputations[0] != null) {
            delayedComputations.remove(0)
        }

        if (neededSignals[i] != null) {
            println("SIGNAL clock: $i register: $xRegister -> ${i * xRegister}")
            signalStrength += (i * xRegister)
            neededSignals[i] = signalStrength
        }

        operations.forEach {
            val opValue = completeOperation(it)
            xRegister += opValue
        }

//        println("current clock $i xRegister=$xRegister -> $operations")
        updateKeys(delayedComputations)
    }


    return signalStrength
}

fun updateKeys(delayedComputations: HashMap<Int, ArrayList<Operation>>) {
    val tmp = HashMap<Int, ArrayList<Operation>>()

    for ((k, v) in delayedComputations) {
        tmp[k - 1] = v
    }

    delayedComputations.clear()
    delayedComputations.putAll(tmp)
}

private fun addCommand(delayedComputations: HashMap<Int, ArrayList<Operation>>, op: Operation, clock: Int) {
    delayedComputations.compute(clock + op.getExecutionTime()) { _, ops ->
        if (ops == null) {
            ArrayList(listOf(op))
        } else {
            ops.add(op)
            ops
        }
    }
}


interface Operation {
    fun getExecutionTime(): Int
}

class NoOp : Operation {
    override fun getExecutionTime(): Int {
        return 1
    }
}

data class AddOp(val operand: Int) : Operation {
    override fun getExecutionTime(): Int {
        return 2
    }
}

private fun completeOperation(op: Operation): Int {
    return when (op) {
        is NoOp -> 0
        is AddOp -> op.operand
        else -> throw IllegalArgumentException("unknown operation")
    }
}


fun day10Part2(input: List<String>): Int {
    return 0
}
