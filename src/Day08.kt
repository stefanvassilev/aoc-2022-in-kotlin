fun main() {
    val input = readInput("Day08")
    println(day8Part1(input))
    println(day8Part2(input))

}

fun day8Part1(input: List<String>): Int {
    val treeMap: Array<Array<Int>> =
            input.map {
                it.toCharArray()
                        .map { ch -> Integer.parseInt(ch.toString()) }.toTypedArray()
            }.toTypedArray()

    var notVisible = 0
    for (i in treeMap.indices) {
        for (j in treeMap[0].indices) {
            if (isEdgeTree(i, treeMap, j)) {
                continue
            }
            if (!isVisibleFromAnyDirection(i, j, treeMap)) {
                notVisible++
            }
        }
    }

    return treeMap[0].size * treeMap.size - notVisible
}

fun day8Part2(input: List<String>): Int {
    val treeMap: Array<Array<Int>> =
            input.map {
                it.toCharArray()
                        .map { ch -> Integer.parseInt(ch.toString()) }.toTypedArray()
            }.toTypedArray()

    var highestScore = Int.MIN_VALUE
    for (i in treeMap.indices) {
        for (j in treeMap[0].indices) {
            // skip edge trees as one of their scores would be 0
            if (isEdgeTree(i, treeMap, j)) {
                continue
            }
            highestScore = Math.max(highestScore, getScenicScoreOfTree(i, j, treeMap))
        }
    }

    return highestScore
}


private fun isVisibleFromAnyDirection(i: Int, j: Int, treeMap: Array<Array<Int>>) =
        isVisibleFromDirection(i, j, Direction.TOP, treeMap) ||
                isVisibleFromDirection(i, j, Direction.BOTTOM, treeMap) ||
                isVisibleFromDirection(i, j, Direction.LEFT, treeMap) ||
                isVisibleFromDirection(i, j, Direction.RIGHT, treeMap)

enum class Direction {
    TOP, BOTTOM, LEFT, RIGHT
}

private fun getScenicScoreOfTree(x: Int, y: Int, treeMap: Array<Array<Int>>): Int {
    return getScenicScoreOfTreeForDirection(x, y, Direction.TOP, treeMap) *
            getScenicScoreOfTreeForDirection(x, y, Direction.BOTTOM, treeMap) *
            getScenicScoreOfTreeForDirection(x, y, Direction.LEFT, treeMap) *
            getScenicScoreOfTreeForDirection(x, y, Direction.RIGHT, treeMap)
}

private fun getScenicScoreOfTreeForDirection(x: Int, y: Int, direction: Direction, treeMap: Array<Array<Int>>): Int {
    val width = treeMap[x].size
    val height = treeMap.size

    val treesToEdge = getTreesToEdge(direction, x, treeMap, y, height, width)
    val currentTree = treeMap[x][y]

    var countViewableTrees = 0
    for (tree in treesToEdge) {
        countViewableTrees++
        if (tree >= currentTree) {
            break
        }

    }
    return countViewableTrees
}

private fun getTreesToEdge(direction: Direction, x: Int, treeMap: Array<Array<Int>>, y: Int, height: Int, width: Int): ArrayList<Int> {
    val treesToEdge = ArrayList<Int>()
    when (direction) {
        Direction.TOP -> {
            for (i in x - 1 downTo 0) {
                treesToEdge.add(treeMap[i][y])
            }
        }

        Direction.BOTTOM -> {
            for (i in x + 1 until height) {
                treesToEdge.add(treeMap[i][y])
            }
        }


        Direction.RIGHT -> {
            for (i in y + 1 until width) {
                treesToEdge.add(treeMap[x][i])
            }
        }

        Direction.LEFT -> {
            for (i in y - 1 downTo 0) {
                treesToEdge.add(treeMap[x][i])
            }
        }
    }
    return treesToEdge
}

private fun isVisibleFromDirection(x: Int, y: Int, direction: Direction, treeMap: Array<Array<Int>>): Boolean {
    val width = treeMap[x].size
    val height = treeMap.size
    val treesToEdge = getTreesToEdge(direction, x, treeMap, y, height, width)

    val currentTree = treeMap[x][y]
    var visible = true
    for (tree in treesToEdge) {
        if (tree >= currentTree) {
            visible = false
            break
        }
    }

    return visible
}

private fun isEdgeTree(i: Int, treeMap: Array<Array<Int>>, j: Int): Boolean {
    return i == 0 || j == 0 || i == treeMap.size - 1 || j == treeMap[0].size - 1
}



