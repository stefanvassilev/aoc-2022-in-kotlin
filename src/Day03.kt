fun main() {
    val input = readInput("Day03")
    println(day3Part1(input))
    println(day3Part2(input))

}


fun day3Part1(input: List<String>): Int {
    return input.stream().map { it: String ->
        val firstCompartment = HashSet<Char>()
        for (firstCompItem in it.toCharArray().take(it.length / 2)) {
            firstCompartment.add(firstCompItem)
        }

        for (secondCompItem in it.toCharArray().takeLast(it.length / 2)) {
            if (firstCompartment.contains(secondCompItem)) {
                return@map secondCompItem.code
            }
        }

        throw IllegalArgumentException("invalid input")
    }.map {
        val c = Char(it)
        return@map calculateCode(c)
    }.reduce(0) { acc, cur -> acc + cur }

}

private fun calculateCode(c: Char): Int {
    val upper = c.isUpperCase()

    val code = c.lowercaseChar().code - 96
    return if (upper) code + 26 else code
}


fun day3Part2(input: List<String>): Int {
    val groups = ArrayList<List<String>>()
    val groupPerRucksack = 3

    val group = ArrayList<String>()
    for (rucksack in input) {
        group.add(rucksack)
        if (group.size == groupPerRucksack) {
            groups.add(ArrayList(group))
            group.clear()
        }
    }

    return groups.map {
        val itemTypesByCount = HashMap<Char, Int>()
        for (elf in it) {
            elf.toSet().toCharArray().forEach { ch -> itemTypesByCount.compute(ch) { k, v -> if (v != null) v + 1 else 1 } }
        }

        for ((k, v) in itemTypesByCount) {
            if (v == groupPerRucksack) {
                val calculateCode = calculateCode(k)
                return@map calculateCode
            }
        }
        throw IllegalArgumentException("bad input")
    }
            .reduce() { acc, cur -> acc + cur }
}


