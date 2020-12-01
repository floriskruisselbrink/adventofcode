package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

class Day1: BaseSolver(2020, 1) {
    override fun part1() {
        val expenses = getInputLines().map(String::toInt)

        for (a in expenses) {
            for (b in expenses) {
                if (a + b == 2020) {
                    val result = a * b
                    println(result)
                    return
                }
            }
        }
    }

    override fun part2() {
        val expenses = getInputLines().map(String::toInt)

        for (a in expenses) {
            for (b in expenses) {
                for (c in expenses) {
                    if (a + b + c == 2020) {
                        val result = a * b * c
                        println(result)
                        return
                    }

                }
            }
        }

    }
}