package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day6.solve()

object Day6 : BaseSolver(2021, 6) {
    val input: List<Int> by lazy {
        getInput().split(",").map(String::toInt)
    }

    override fun part1(): Long = solve(80)
    override fun part2(): Long = solve(256)

    private fun solve(days: Int): Long {
        var lanterns = input
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }

        for (day in 1..days) {
            lanterns = nextIteration(lanterns)
        }

        return lanterns.values.sum()
    }

    private fun nextIteration(lanterns: Map<Int, Long>): Map<Int, Long> {
        val newLanterns = mutableMapOf<Int, Long>()

        for (fish in 0..8) {
            newLanterns[fish] = when (fish) {
                8 -> lanterns.getOrDefault(0, 0)
                6 -> lanterns.getOrDefault(0, 0) + lanterns.getOrDefault(fish + 1, 0)
                else -> lanterns.getOrDefault(fish + 1, 0)
            }
        }

        return newLanterns
    }

    private fun getTestInput(): String = "3,4,3,1,2"
}