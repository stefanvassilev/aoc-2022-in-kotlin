fun main() {
    val input = readInput("Day12-sample")
    println(day12Part1(input))
    println(day12Part2(input))

}


data class GraphNode(val elevation: Char, val x: Int, val y: Int, var distance: Int = Int.MAX_VALUE) {
    val adjacent = ArrayList<GraphNode>()
    val shortestPath = ArrayList<GraphNode>()
}

fun day12Part1(input: List<String>): Int {
    val map = input.map { it.toCharArray() }
    val rowSize = map.size
    val colSize = map[0].size
    val (startX, startY) = getCoordinates(map, 'S')
    val startNode = GraphNode(map[startX][startY], startX, startY)


    val visited = HashSet<GraphNode>()
    val toVisit = HashSet<GraphNode>()
    toVisit.add(startNode)

    while (toVisit.isNotEmpty()) {
        val current = toVisit.first()

        val adjacent = ArrayList<GraphNode>()
        if (current.x + 1 < rowSize) {
            adjacent.add(GraphNode(map[current.x + 1][current.y], current.x + 1, current.y))
        }
        if (current.x - 1 >= 0) {
            adjacent.add(GraphNode(map[current.x - 1][current.y], current.x - 1, current.y))
        }
        if (current.y + 1 < colSize) {
            adjacent.add(GraphNode(map[current.x][current.y + 1], current.x, current.y + 1))
        }
        if (current.y - 1 >= 0) {
            adjacent.add(GraphNode(map[current.x][current.y - 1], current.x, current.y - 1))
        }


        if (current.elevation == 'E') {
            println("current: ${current}")
        }

        val reachableAdjacent = adjacent
                .filter { isReachable(current.elevation, it.elevation) }
                .filter { visited.none { tv -> tv.x == it.x && tv.y == it.y } }
        toVisit.addAll(reachableAdjacent)
        current.adjacent.addAll(reachableAdjacent)
        toVisit.remove(current)
        visited.add(current)
    }

    println(startNode)
    calculateShortestPath(startNode)
    val endNode = findEndNode(startNode)

    println("distance: ${dfs(startNode, endNode!!)}")
    println(endNode)
    println(endNode!!.distance)

    println("Shortest path")
    endNode.shortestPath.forEach {
        println("shortest path: $it")
    }

    return endNode!!.distance
}

fun findEndNode(node: GraphNode): GraphNode? {
    if (node.elevation == 'E') {
        return node
    }
    for (adj in node.adjacent) {
        val res = findEndNode(adj)
        if (res != null) {
            return res
        }
    }

    return null
}

fun calculateShortestPath(start: GraphNode) {
    start.distance = 0
    val settled = HashSet<GraphNode>()

    val unsettled = HashSet<GraphNode>()
    unsettled.add(start)

    while (unsettled.isNotEmpty()) {
        val cur = getLowestDistanceNode(unsettled)
        unsettled.remove(cur)
        println("CUR: ${cur} neighbours: ${cur.adjacent}")
        for (adj in cur.adjacent) {
            if (!settled.contains(adj)) {
                calculateLowestDistance(cur, adj)
                unsettled.add(adj)
            }
        }
        settled.add(cur)
    }
}


private fun calculateLowestDistance(cur: GraphNode, adj: GraphNode) {
    if (cur.distance + 1 < adj.distance) {
        adj.distance = cur.distance + 1
        adj.shortestPath.addAll(cur.shortestPath)
        adj.shortestPath.add(cur)
    }
}

fun getLowestDistanceNode(nodes: Set<GraphNode>): GraphNode {
    var lowestDistance = Int.MAX_VALUE
    var lowestDistanceNode: GraphNode? = null

    for (node in nodes) {
        if (node.distance < lowestDistance) {
            lowestDistance = node.distance
            lowestDistanceNode = node
        }
    }

    return lowestDistanceNode!!
}

val elevationMap = listOf('a'..'z').flatten()

fun isReachable(curElevation: Char, targetElevation: Char): Boolean {
    if (targetElevation == 'E') {
        return curElevation == 'z'
    }
    if (curElevation == 'S') {
        return targetElevation == 'a'
    }

    return elevationMap.indexOf(targetElevation) <= elevationMap.indexOf(curElevation) + 1
}

fun getCoordinates(map: List<CharArray>, elevation: Char): Pair<Int, Int> {
    for (row in map.indices) {
        for (col in map.indices) {
            if (map[row][col] == elevation) {
                return Pair(row, col)
            }
        }
    }
    throw IllegalArgumentException("cannot find elevation")
}


fun day12Part2(input: List<String>): Int {
    return 0
}

