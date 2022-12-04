fun main() {
    val input = readInput("Day04")
    println(day4Part1(input))
    println(day4Part2(input))

}


fun day4Part1(input: List<String>): Int {
    return input.map { convertPair(it) }.filter {
        val pair1 = it.first
        val pair2 = it.second

        return@filter pair1.first <= pair2.first && pair1.second >= pair2.second ||
                pair2.first <= pair1.first && pair2.second >= pair1.second
    }.count()
}

private fun convertPair(input: String): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    val pairs = input.split(",")

    return Pair<Pair<Int, Int>, Pair<Int, Int>>(
            Pair(Integer.parseInt(pairs[0].substringBefore('-')),
                    Integer.parseInt(pairs[0].substringAfter('-'))),
            Pair(Integer.parseInt(pairs[1].substringBefore('-')),
                    Integer.parseInt(pairs[1].substringAfter('-'))))
}


fun day4Part2(input: List<String>): Int {
    return input.map { convertPair(it) }.filter {
        val pair1 = it.first
        val pair2 = it.second

        return@filter isOverlapping(pair1, pair2)
    }.count()
}

private fun isOverlapping(pair1: Pair<Int, Int>, pair2: Pair<Int, Int>): Boolean {
    return (pair2.first >= pair1.first && pair2.first <= pair1.second ||
            pair2.second >= pair1.first && pair2.second <= pair1.second) ||
            (pair1.first >= pair2.first && pair1.first <= pair2.second ||
                    pair1.second >= pair2.first && pair1.second <= pair2.second)
}



