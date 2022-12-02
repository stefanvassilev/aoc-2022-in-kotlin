import OpponentMove.*

fun main() {
    val input = readInput("Day02")
    println(day2Part1(input))
    println(day2Part2(input))

}

enum class OpponentMove(val code: String) {
    ROCK("A"),
    PAPER("B"),
    SCISSORS("C");

    companion object {
        fun decrypt(code: String): OpponentMove {
            return values().first { it.code == code }
        }
    }
}

enum class PlayerMove(val code: String) {
    ROCK("X"),
    PAPER("Y"),
    SCISSORS("Z");


    companion object {
        fun decrypt(code: String): PlayerMove {
            return values().first { it.code == code }
        }
    }
}

enum class ResultMapping(val code: String) {
    WIN("Z"), LOSE("X"), DRAW("Y");

    companion object {
        fun decrypt(code: String): ResultMapping {
            return values().first { it.code == code }
        }
    }
}

fun mapToShape(opponentMove: OpponentMove, resultMapping: ResultMapping): PlayerMove {
    return when (resultMapping) {
        ResultMapping.WIN -> {
            return when (opponentMove) {
                ROCK -> PlayerMove.PAPER
                PAPER -> PlayerMove.SCISSORS
                SCISSORS -> PlayerMove.ROCK
            }
        }

        ResultMapping.LOSE -> {
            return when (opponentMove) {
                ROCK -> PlayerMove.SCISSORS
                PAPER -> PlayerMove.ROCK
                SCISSORS -> PlayerMove.PAPER
            }
        }

        ResultMapping.DRAW -> {
            return when (opponentMove) {
                ROCK -> PlayerMove.ROCK
                PAPER -> PlayerMove.PAPER
                SCISSORS -> PlayerMove.SCISSORS
            }
        }
    }
}


enum class ShapeScore(val value: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    companion object {
        fun fromPlayerMove(move: PlayerMove): ShapeScore {
            return when (move) {
                PlayerMove.PAPER -> PAPER
                PlayerMove.ROCK -> ROCK
                PlayerMove.SCISSORS -> SCISSORS
            }
        }
    }

}


fun day2Part1(input: List<String>): Int {
    val pairs = getPairs(input)
    var points = 0
    for (it in pairs) {
        points += getPointsFromPair(it)
    }

    return points
}

private fun getPointsFromPair(it: Pair<OpponentMove, PlayerMove>): Int {
    var points = 0
    when (it.first) {
        ROCK -> {
            points += when (it.second) {
                PlayerMove.PAPER -> 6 // win
                PlayerMove.ROCK -> 3 // draw
                PlayerMove.SCISSORS -> 0 // lose
            }
        }

        PAPER -> {
            points += when (it.second) {
                PlayerMove.SCISSORS -> 6 // win
                PlayerMove.PAPER -> 3 // draw
                PlayerMove.ROCK -> 0 // lose
            }
        }

        SCISSORS -> {
            points += when (it.second) {
                PlayerMove.ROCK -> 6 // win
                PlayerMove.SCISSORS -> 3 // draw
                PlayerMove.PAPER -> 0 // lose
            }
        }
    }
    points += ShapeScore.fromPlayerMove(it.second).value
    return points
}

fun getPairs(input: List<String>): List<Pair<OpponentMove, PlayerMove>> {
    return input.map {
        Pair(OpponentMove.decrypt("" + it.first()),
                PlayerMove.decrypt("" + it.last()))
    }.toList()
}

fun day2Part2(input: List<String>): Int {
    val pairs = getPairsPart2(input)
    var points = 0
    for (it in pairs) {
        points += getPointsFromPair(it)
    }

    return points
}


fun getPairsPart2(input: List<String>): List<Pair<OpponentMove, PlayerMove>> {
    return input.map {
        val opponentMove = OpponentMove.decrypt("" + it.first())
        Pair(opponentMove,
                mapToShape(opponentMove, ResultMapping.decrypt("" + it.last())))
    }.toList()
}
