package nl.vloris.adventofcode.common

abstract class BaseSolver(
    val year: Int,
    val day: Int
) {
    abstract fun part1()
    abstract fun part2()

    fun run() {
        println("=== $year day $day ===")
        print("-- Part1: ")
        part1()

        print("-- Part2: ")
        part2()
    }

    protected fun getInputLines(): List<String> {
        return getInput().lines().filter { it.isNotEmpty() }
    }

    protected fun getInput(): String {
        return InputDownloader(year, day).getInput()
    }
}