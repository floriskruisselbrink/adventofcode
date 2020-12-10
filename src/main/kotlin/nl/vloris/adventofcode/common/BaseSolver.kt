package nl.vloris.adventofcode.common

import kotlin.system.measureTimeMillis

abstract class BaseSolver(
    val year: Int,
    val day: Int
) {
    abstract fun part1()
    abstract fun part2()

    fun run() {
        println("=== $year day $day ===")
        print("-- Part1: ")
        val elapsed1 = measureTimeMillis {
            part1()
        }
        println("-- Part1 took $elapsed1 milliseconds")

        print("-- Part2: ")
        val elapsed2 = measureTimeMillis {
            part2()
        }
        println("-- Part2 took $elapsed2 milliseconds")
    }

    protected fun getInputLines(): List<String> {
        return getInput().lines().filter { it.isNotEmpty() }
    }

    protected fun getInput(): String {
        return InputDownloader(year, day).getInput()
    }
}