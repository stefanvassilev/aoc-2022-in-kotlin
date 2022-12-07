import java.util.*

fun main() {
    val input = readInput("Day07")
    println(day7Part1(input))
    println(day7Part2(input))

}

interface ExplorerItem

class Directory(var parent: Directory?, val dirName: String, var dirSize: Int, var contents: ArrayList<ExplorerItem>) : ExplorerItem {

    fun addContents(vararg contents: ExplorerItem) {
        contents.forEach { this.contents.add(it) }
    }

    fun calculateSize(): Int {
        var size = 0
        contents.forEach {

            when (it) {
                is Directory -> {
                    size += it.calculateSize()
                }

                is File -> {
                    size += it.size
                }
            }
        }

        this.dirSize = size
        return dirSize
    }

    fun traverseDirectories(calc: (Directory) -> Unit) {
        calc(this)
        contents.filterIsInstance<Directory>().forEach {
            it.traverseDirectories(calc)

        }

    }

    override fun toString(): String {
        return "Directory(dirName:${dirName},parent=${if (parent != null) parent!!.dirName else "root node"}, contents size=${contents.size})"
    }


}

data class File(val fileName: String, val size: Int) : ExplorerItem


fun day7Part1(input: List<String>): Int {
    val root = Directory(null, "/", 0, ArrayList())
    traverseInput(root, input.subList(1, input.size))
    root.calculateSize()

    val dirs = ArrayList<Directory>()
    var sum = 0

    root.traverseDirectories { directory ->
        if (directory.dirSize <= 100000) {
            dirs.add(directory)
            sum += directory.dirSize
        }
    }


    return sum
}


private fun traverseInput(curDir: Directory, input: List<String>) {
    var curIndex = 0
    while (curIndex < input.size) {

        val line = input[curIndex]

        when {
            line.contains("$ cd ..") -> {
                traverseInput(curDir.parent!!, input.subList(curIndex + 1, input.size))
                return
            }

            line.contains("$ cd") -> {
                val dirName = line.substringAfter("cd ")
                val dirToWalk = curDir.contents.first() { it is Directory && it.dirName == dirName } as Directory

                traverseInput(dirToWalk, input.subList(curIndex + 1, input.size))
                return
            }

            line.contains("ls") -> {
                val (contents, walked) = walkInputUntilShellCommand(curDir, input.subList(curIndex + 1, input.size))
                curIndex += walked
                curDir.addContents(*contents.toTypedArray())
            }

        }

        curIndex++
    }


}

private fun walkInputUntilShellCommand(curDir: Directory, input: List<String>): Pair<List<ExplorerItem>, Int> {
    val res = ArrayList<ExplorerItem>()
    var curIndex = 0

    for (it in input) {
        when {
            it.contains("dir") -> {
                res.add(Directory(curDir, it.substringAfter("dir "), 0, ArrayList()))
            }

            it.contains("$") -> {
                return Pair(res, curIndex)
            }

            else -> {
                val stringList = it.split(" ")
                val size = stringList[0].toInt()
                val fileName = stringList[1]

                res.add(File(fileName, size))
            }
        }
        curIndex++
    }

    return Pair(res, curIndex)
}


fun day7Part2(input: List<String>): Int {
    val root = Directory(null, "/", 0, ArrayList())
    traverseInput(root, input.subList(1, input.size))
    root.calculateSize()

    val totalSpace = 70000000
    val neededSpace = 30000000
    val spaceToFree = neededSpace - (totalSpace - root.dirSize)

    val eligibleDirSizes = TreeSet<Int>()
    root.traverseDirectories { dir ->
        if (dir.dirSize > spaceToFree) {
            eligibleDirSizes.add(dir.dirSize)
        }
    }

    return eligibleDirSizes.first()
}

