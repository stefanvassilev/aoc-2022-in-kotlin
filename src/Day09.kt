import kotlin.math.abs

fun main() {
    val input = readInput("Day09")
    println(day9Part1(input))
    println(day9Part2(input))

}

data class Coordinates(var x: Int, var y: Int)


class Node(val coordinates: Coordinates) {
    val positionHistory: ArrayList<Coordinates> = ArrayList()


    fun addToHistory(nextPosition: Coordinates) {
        positionHistory.add(nextPosition)
    }

    fun move(next: Coordinates) {
        this.coordinates.x += next.x
        this.coordinates.y += next.y
    }

}


enum class MovementDirection {
    UP, DOWN, LEFT, RIGHT, NONE;

    companion object {
        fun createFromLetter(letter: String): MovementDirection {
            return when (letter) {
                "U" -> UP
                "D" -> DOWN
                "L" -> LEFT
                "R" -> RIGHT
                else -> throw IllegalArgumentException("Unknown movement $letter, possible values are ${values()}")
            }
        }
    }


}

fun day9Part1(input: List<String>): Int {
    val head = Node(Coordinates(0, 0))
    val tail = Node(Coordinates(0, 0))

    val nextHeadPositions = getHeadMovements(input)
    for (nextMove in nextHeadPositions) {
        val oldHeadPosition = head.coordinates.copy()
        head.move(nextMove)
        moveAdjacentNodes(head, tail, oldHeadPosition)
    }

    return tail.positionHistory.toSet().size + 1
}

fun day9Part2(input: List<String>): Int {
    val nodes = ArrayList<Node>()
    for (i in 0 until 10) {
        nodes.add(Node(Coordinates(0, 0)))
    }

    val nextHeadPositions = getHeadMovements(input)

    for (nextMove in nextHeadPositions) {
        var leader = nodes[0]
        var oldLeaderPosition: Coordinates
        leader.move(nextMove)
        for (follower in nodes.subList(1, nodes.size)) {
            oldLeaderPosition = follower.coordinates.copy()
            moveAdjacentNodes(leader, follower, oldLeaderPosition)
            leader = follower

        }
    }

    return nodes.last().positionHistory.toSet().size + 1
}

fun moveAdjacentNodes(head: Node, tail: Node, oldHeadPosition: Coordinates) {
    val directionFromCoordinates = getDirectionFromCoordinates(oldHeadPosition, head.coordinates)

    val xChange = getXChange(head.coordinates.x - tail.coordinates.x)
    val yChange = getYChange(head.coordinates.y - tail.coordinates.y)
    val nextTailMove = getNextTailMovement(directionFromCoordinates, xChange, yChange)

    val tailNextCoordinates = Coordinates(tail.coordinates.x + nextTailMove.x, tail.coordinates.y + nextTailMove.y)
    val overlapping = coordinatesOverlapping(tailNextCoordinates, head.coordinates)
    if (overlapping || proximityIsLessThanOne(tail.coordinates, head.coordinates)) {
        return
    }

    tail.addToHistory(tail.coordinates.copy())
    tail.move(nextTailMove)

}

private fun getHeadMovements(input: List<String>): List<Coordinates> {
    val nextHeadPositions = input.map {
        val coordinatesRaw = it.split(" ")
        val moveDirection = MovementDirection.createFromLetter(coordinatesRaw[0])
        val xy = coordinatesRaw[1].toInt()

        val changes = ArrayList<Coordinates>()
        for (i in 0 until xy) {
            when (moveDirection) {
                MovementDirection.UP -> changes.add(Coordinates(0, 1))
                MovementDirection.DOWN -> changes.add(Coordinates(0, -1))
                MovementDirection.LEFT -> changes.add(Coordinates(-1, 0))
                MovementDirection.RIGHT -> changes.add(Coordinates(1, 0))
                MovementDirection.NONE -> throw IllegalArgumentException("")
            }
        }
        changes
    }.flatten()
    return nextHeadPositions
}

private fun getNextTailMovement(directionFromCoordinates: MovementDirection, xChange: Int, yChange: Int): Coordinates {
    return when (directionFromCoordinates) {
        MovementDirection.UP -> {
            Coordinates(xChange, 1)
        }

        MovementDirection.DOWN -> {
            Coordinates(xChange, -1)
        }

        MovementDirection.LEFT -> {
            Coordinates(-1, yChange)
        }

        MovementDirection.RIGHT -> {
            Coordinates(1, yChange)

        }

        MovementDirection.NONE -> {
            Coordinates(0, 0)
        }
    }
}

private fun getXChange(xDelta: Int) = if (xDelta > 0) {
    1
} else if (xDelta < 0) -1 else {
    0
}

private fun getYChange(yDelta: Int) = if (yDelta > 0) {
    1
} else if (yDelta < 0) {
    -1
} else {
    0
}

fun coordinatesOverlapping(cord1: Coordinates, cord2: Coordinates): Boolean {
    return cord1.x == cord2.x && cord1.y == cord2.y
}

fun proximityIsLessThanOne(cord1: Coordinates, cord2: Coordinates): Boolean {
    return abs(cord1.y - cord2.y) <= 1 && abs(cord1.x - cord2.x) <= 1
}

fun getDirectionFromCoordinates(cord1: Coordinates, cord2: Coordinates): MovementDirection {
    val xDelta = cord2.x - cord1.x
    when {
        xDelta > 0 -> return MovementDirection.RIGHT
        xDelta < 0 -> return MovementDirection.LEFT
    }

    val yDelta = cord2.y - cord1.y
    when {
        yDelta > 0 -> return MovementDirection.UP
        yDelta < 0 -> return MovementDirection.DOWN
    }

    return MovementDirection.NONE
}
