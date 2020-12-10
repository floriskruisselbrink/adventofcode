package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day1.solve()

object Day1 : BaseSolver(2020, 1) {
    override fun part1(): Int? {
        val expenses = getInputLines().map(String::toInt)

        for (a in expenses) {
            for (b in expenses) {
                if (a + b == 2020) {
                    return a * b
                }
            }
        }

        return null
    }

    override fun part2(): Int? {
        val expenses = getInputLines().map(String::toInt)

        for (a in expenses) {
            for (b in expenses) {
                for (c in expenses) {
                    if (a + b + c == 2020) {
                        return a * b * c
                    }
                }
            }
        }

        return null
    }
}