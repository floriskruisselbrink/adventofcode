package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day1.solve()

object Day1 : BaseSolver(2021, 1) {

    override fun part1(): Int =
        getInputLines().map(String::toInt)
            .windowed(2)
            .count { it[1] > it[0] }

    override fun part2(): Int =
        getInputLines().map(String::toInt)
            .windowed(3)
            .map { it.sum() }
            .windowed(2)
            .count { it[1] > it[0] }
}