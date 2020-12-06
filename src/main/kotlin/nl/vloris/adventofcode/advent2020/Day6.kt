package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

class Day6 : BaseSolver(2020, 6) {
    override fun part1() {
        val groups = getInput()
            .split("\n\n")
            .map { group ->
                group.replace("\n", "")
                    .toList()
                    .sorted()
                    .distinct()
                    .count()
            }

        println(groups.sum())
    }

    override fun part2() {
        val groups = getInput()
            .split("\n\n")
            .map { group ->
                group
                    .split("\n")
                    .map { person -> person.toList().sorted() }
                    .filter { person -> person.isNotEmpty() }
                    .reduce { acc, person -> (acc intersect person).toList() }
                    .count()
            }

        println(groups.sum())
    }
}