import java.util.Arrays
import java.util.ListResourceBundle

fun main() {
    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))

}

fun part1(input: List<String>): Int {
    return getElvesFood(input)
            .map { it.reduce { acc, next -> acc + next } }
            .max();
}

fun part2(input: List<String>): Int {
    val sortedList = getElvesFood(input)
            .map { it.reduce { acc, next -> acc + next } }
            .sorted()

    return sortedList.subList(sortedList.size - 3, sortedList.size).reduce{acc, next -> acc + next}
}

private fun getElvesFood(input: List<String>): ArrayList<List<Int>> {
    val elvesFood = ArrayList<List<Int>>()

    var curFood = ArrayList<Int>()
    for (line in input) {
        if (line == "") {
            elvesFood.add(curFood);
            curFood = ArrayList();
        } else {
            curFood.add(Integer.parseInt(line))
        }
    }
    return elvesFood
}


