package nl.vloris.adventofcode.common

import kotlin.system.measureNanoTime

abstract class BaseSolver(
    val year: Int,
    val day: Int
) {
    abstract fun part1(): Any?
    abstract fun part2(): Any?

    fun solve() {
        println("=== $year day $day ===")
        printSolution { part1() }
        printSolution { part2() }
    }

    private fun printSolution(solve: () -> Any?) {
        var solution: Any?
        val time = measureNanoTime { solution = solve() } / 1_000_000
        val formattedTime = time.toString().padStart(4)
        println("[$formattedTime ms] $solution")
    }

    protected fun getInputLines(): List<String> {
        return getInput().lines().filter { it.isNotEmpty() }
    }

    protected fun getInput(): String {
        return retrieveInput(year, day)
    }
}