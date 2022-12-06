fun main() {
    val input = readInput("Day06")
    println(day6Part1(input))
    println(day6Part2(input))

}


fun day6Part1(input: List<String>): Int {
    return detectMessage(input[0], 4)
}


fun day6Part2(input: List<String>): Int {
    return detectMessage(input[0], 14)
}

private fun distinctBufferCharacters(buffer: List<Char>, distinctCharacterCount: Int): Boolean {
    return buffer.toSet().size == distinctCharacterCount

}

private fun detectMessage(data: String, distinctCharacterCount: Int): Int {
    val window = ArrayList<Char>()

    var processedCharacters = 0;
    for (char in data) {
        window.add(char)
        processedCharacters++

        if (window.size >= distinctCharacterCount) {
            if (distinctBufferCharacters(window, distinctCharacterCount)) {
                return processedCharacters
            }
            window.removeFirst()
        }
    }

    return processedCharacters
}

