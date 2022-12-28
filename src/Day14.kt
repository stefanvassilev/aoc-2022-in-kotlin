import java.util.*
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = readInput("Day14")
    println(day14Part1(input))
    println(day14Part2(input))

}


data class Line(val points: Pair<Point, Point>)
data class Point(var x: Int, var y: Int)


fun day14Part1(input: List<String>): Int {
    val lines = getLinesFromScan(input)
    lines.forEach { println(it) }

    val points = lines.map { listOf(it.points.first, it.points.second) }.flatten()
    val maxRightDistance = points.maxBy { it.x }.x
    val minRightDistance = points.minBy { it.x }.x

    val maxDownDistance = points.maxBy { it.y }.y
    val minDownDistance = 0 //points.minBy { it.y }.y

    val matrix = createMatrixFromInput(maxDownDistance, minDownDistance, maxRightDistance, minRightDistance, lines)
    val sandPoint = Point(500 - minRightDistance, 0)
    matrix[sandPoint.y][sandPoint.x] = '+'


    var sandPointFound = false
    var potentialSpot = Point(sandPoint.x, sandPoint.y + 1)

    val curPath = LinkedList<Point>()
    while (true) {
        val neighbourPoints = getNeighbourPoints(matrix, potentialSpot)

        val bottom = neighbourPoints[0]
        val downLeft = neighbourPoints[1]
        val downRight = neighbourPoints[2]


        if (downLeft == null || downRight == null || bottom == null) {
            println("Abyss reached: ${curPath.size}")
            break
        }

        if (isPathBlocked(matrix, bottom) && isPathBlocked(matrix, downLeft) && isPathBlocked(matrix, downRight)) {
            sandPointFound = true
        } else if (!isPathBlocked(matrix, bottom)) {
            potentialSpot.x = bottom.x
            potentialSpot.y = bottom.y
        } else if (!isPathBlocked(matrix, downLeft)) {
            potentialSpot.x = downLeft.x
            potentialSpot.y = downLeft.y
        } else if (!isPathBlocked(matrix, downRight)) {
            potentialSpot.x = downRight.x
            potentialSpot.y = downRight.y
        }


        if (sandPointFound) {
            curPath.add(Point(potentialSpot.x, potentialSpot.y))
            matrix[potentialSpot.y][potentialSpot.x] = 'o'
            sandPointFound = false
            potentialSpot = Point(sandPoint.x, sandPoint.y + 1)
            matrix.forEach { println(it.contentToString()) }
        }
    }


    matrix.forEach { println(it.contentToString()) }
    return curPath.size
}

fun day14Part2(input: List<String>): Int {
    val lines = getLinesFromScan(input)

    val points = lines.map { listOf(it.points.first, it.points.second) }.flatten()
    val maxRightDistance = points.maxBy { it.x }.x
    val minRightDistance = points.minBy { it.x }.x

    val maxDownDistance = points.maxBy { it.y }.y
    val minDownDistance = 0 //points.minBy { it.y }.y

    val xOffset = 500
    val matrix = createMatrixFromInput(maxDownDistance + 2, minDownDistance, maxRightDistance + 1000, minRightDistance, lines, xOffset = xOffset)
    val sandPoint = Point(500 - minRightDistance + xOffset, 0)
    matrix[sandPoint.y][sandPoint.x] = '+'

    for (i in matrix[matrix.size - 1].indices) {
        matrix[matrix.size - 1][i] = '#'
    }


    var sandPointFound = false
    var potentialSpot = Point(sandPoint.x, sandPoint.y)

    val curPath = LinkedList<Point>()
    while (true) {
        val neighbourPoints = getNeighbourPoints(matrix, potentialSpot)

        val bottom = neighbourPoints[0]
        val downLeft = neighbourPoints[1]
        val downRight = neighbourPoints[2]


        if (downLeft == null || downRight == null || bottom == null) {
            println("Abyss reached: ${curPath.size}")
            break
        }
        if (potentialSpot.x == sandPoint.x && potentialSpot.y == sandPoint.y &&
                matrix[bottom.y][bottom.x] == 'o' &&
                matrix[downLeft.y][downLeft.x] == 'o' &&
                matrix[downRight.y][downRight.x] == 'o'
        ) {
            curPath.add(Point(potentialSpot.x, potentialSpot.y))
            matrix[potentialSpot.y][potentialSpot.x] = 'o'
            matrix.forEach { println(it.contentToString()) }
            break
        }

        if (isPathBlocked(matrix, bottom) && isPathBlocked(matrix, downLeft) && isPathBlocked(matrix, downRight)) {
            sandPointFound = true
        } else if (!isPathBlocked(matrix, bottom)) {
            potentialSpot.x = bottom.x
            potentialSpot.y = bottom.y
        } else if (!isPathBlocked(matrix, downLeft)) {
            potentialSpot.x = downLeft.x
            potentialSpot.y = downLeft.y
        } else if (!isPathBlocked(matrix, downRight)) {
            potentialSpot.x = downRight.x
            potentialSpot.y = downRight.y
        }


        if (sandPointFound) {
            curPath.add(Point(potentialSpot.x, potentialSpot.y))
            matrix[potentialSpot.y][potentialSpot.x] = 'o'
            sandPointFound = false
            potentialSpot = Point(sandPoint.x, sandPoint.y)
        }
    }


    return curPath.size
}

private fun createMatrixFromInput(maxDownDistance: Int, minDownDistance: Int, maxRightDistance: Int, minRightDistance: Int, lines: List<Line>, xOffset: Int = 0): Array<Array<Char>> {
    val matrix = Array(size = maxDownDistance - minDownDistance + 1) { Array(maxRightDistance - minRightDistance + 1) { '.' } }

    println("size ${matrix.size}x${matrix[0].size}")

    for (line in lines) {
        val (firstPoint, secondPoint) = line.points

        val xStart = min(firstPoint.x - minRightDistance, secondPoint.x - minRightDistance) + xOffset
        val xEnd = max(secondPoint.x - firstPoint.x, firstPoint.x - secondPoint.x) + xStart

        val yEnd = (if (firstPoint.y < secondPoint.y) secondPoint.y else firstPoint.y)
        val yStart = (if (firstPoint.y < secondPoint.y) firstPoint.y else secondPoint.y)


        var curX = xStart
        var curY = yStart

//        println("Line: first point: $firstPoint second point: $secondPoint curX: $curX < $xEnd || curY: $curY < $yEnd")
        while (true) {
            matrix[curY][curX] = '#'
            if (xStart != xEnd) {
                curX++
            }
            if (yStart != yEnd) {
                curY++
            }

            if (curX == xEnd + 1 && curY == yEnd) {
                break
            }
            if (curY == yEnd + 1 && curX == xEnd) {
                break
            }
        }

    }
    return matrix
}

private fun isPathBlocked(matrix: Array<Array<Char>>, point: Point?): Boolean {
//    println("neighbour point: $point")
    return point != null && (matrix[point.y][point.x] == '#' || matrix[point.y][point.x] == 'o')
}

private fun getNeighbourPoints(matrix: Array<Array<Char>>, potentialSpot: Point): ArrayList<Point?> {
    val neighbours = arrayOf(Pair(0, 1), Pair(-1, 1), Pair(1, 1)) // down below, down left, down right
    val neighbourPoints = ArrayList<Point?>()
    for (neighbour in neighbours) {
        if (isNeighbourInBoundary(matrix, neighbour, potentialSpot)) {
            neighbourPoints.add(Point(potentialSpot.x + neighbour.first, potentialSpot.y + neighbour.second))
        } else {
            neighbourPoints.add(null)
        }
    }
    return neighbourPoints
}


fun isNeighbourInBoundary(matrix: Array<Array<Char>>, neighbour: Pair<Int, Int>, potentialSpot: Point): Boolean {
    return (potentialSpot.x + neighbour.first < matrix[0].size && potentialSpot.x + neighbour.first >= 0) &&
            (potentialSpot.y + neighbour.second < matrix.size && potentialSpot.y + neighbour.second >= 0)
}

private fun getLinesFromScan(input: List<String>): List<Line> {
    return input.map {
        val pointsOnLine = it.split(" -> ").map { coords ->
            Point(coords.split(",")[0].toInt(), coords.split(",")[1].toInt())
        }

        val lines = ArrayList<Line>()
        for (i in 0 until pointsOnLine.size - 1) {
            val point1 = pointsOnLine[i]
            val point2 = pointsOnLine[i + 1]
            lines.add(Line(Pair(point1, point2)))
        }
        lines
    }.flatten().toList()
}

