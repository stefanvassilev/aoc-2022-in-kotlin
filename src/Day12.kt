import java.util.*

fun main() {
    val input = readInput("Day12")
    println(day12Part1(input))
    println(day12Part2(input))

}


data class GraphNode(val elevation: Char, val x: Int, val y: Int)

fun day12Part1(input: List<String>): Int {
    val map = input.map { it.toCharArray() }
    val startCoords = getCoordinates(map, 'S')
    val endCoords = getCoordinates(map, 'E')

    val nodes = HashMap<Pair<Int, Int>, GraphNode>()
    for (x in map.indices) {
        for (y in map[x].indices) {
            nodes[Pair(x, y)] = GraphNode(map[x][y], x, y)
        }
    }
    return getShortestPathFromPointToPoint(nodes, startCoords, endCoords)

}

val elevationMap = listOf('a'..'z').flatten()

fun height(char: Char): Int {
    if (char == 'S') {
        return 0
    }
    if (char == 'E') {
        return 25
    }
    return elevationMap.indexOf(char)
}

fun getCoordinates(map: List<CharArray>, elevation: Char): Pair<Int, Int> {
    for (row in map.indices) {
        for (col in map[0].indices) {
            if (map[row][col] == elevation) {
                return Pair(row, col)
            }
        }
    }
    throw IllegalArgumentException("cannot find elevation")
}

fun getAllCoordinates(map: List<CharArray>, elevation: Char): List<Pair<Int, Int>> {
    val pairs = HashSet<Pair<Int, Int>>()
    for (row in map.indices) {
        for (col in map[0].indices) {
            val element = Pair(row, col)
            if (map[row][col] == elevation && !pairs.contains(element)) {
                pairs.add(element)
            }
        }
    }

    return pairs.toList()
}


fun day12Part2(input: List<String>): Int {
    val map = input.map { it.toCharArray() }
    val endCoord = getCoordinates(map, 'E')
    val coordinates = getAllCoordinates(map, 'a')

    val nodes = HashMap<Pair<Int, Int>, GraphNode>()
    for (x in map.indices) {
        for (y in map[x].indices) {
            nodes[Pair(x, y)] = GraphNode(map[x][y], x, y)
        }
    }

    val results = ArrayList<Int>()
    for (startCoordinate in coordinates) {
        val element = getShortestPathFromPointToPoint(nodes, startCoordinate, endCoord)
        results.add(element)
    }

    return results.min()
}

private fun getShortestPathFromPointToPoint(nodes: HashMap<Pair<Int, Int>, GraphNode>, startCoords: Pair<Int, Int>, endCoords: Pair<Int, Int>): Int {
    val startNode = nodes[startCoords]!!

    val deque: Deque<Pair<GraphNode, Int>> = LinkedList()
    deque.push(Pair(startNode, 0))
    val visited = HashSet<GraphNode>()

    while (true) {
        if (deque.isEmpty()) {
            return Int.MAX_VALUE
        }
        val (cur, steps) = deque.pollLast()
        if (visited.contains(cur)) {
            continue
        }
        visited.add(cur)
        if (cur.x == endCoords.first && cur.y == endCoords.second) {
            return steps
        }


        val neighbours = arrayOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))
        for (neighbour in neighbours) {
            val neighbourCoords = Pair(cur.x + neighbour.first, cur.y + neighbour.second)
            if (nodes.containsKey(neighbourCoords) && height(nodes[neighbourCoords]!!.elevation) <= height(cur.elevation) + 1) {
                deque.push(Pair(nodes[neighbourCoords]!!, steps + 1))
            }
        }

    }
}

