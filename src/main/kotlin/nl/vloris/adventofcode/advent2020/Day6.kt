package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day6.solve()

object Day6 : BaseSolver(2020, 6) {
    override fun part1(): Int =
        getInput()
            .split("\n\n")
            .map { group ->
                group.replace("\n", "")
                    .toList()
                    .sorted()
                    .distinct()
                    .count()
            }
            .sum()

    override fun part2(): Int =
        getInput()
            .split("\n\n")
            .map { group ->
                group
                    .split("\n")
                    .map { person -> person.toList().sorted() }
                    .filter { person -> person.isNotEmpty() }
                    .reduce { acc, person -> (acc intersect person).toList() }
                    .count()
            }
            .sum()
}