package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day15.solve()

object Day15 : BaseSolver(2020, 15) {
    override fun part1(): Int {
        return calculateNumber(2020)
    }

    override fun part2(): Int {
        return calculateNumber(30_000_000)
    }

    private fun calculateNumber(n: Int): Int {
        val input = getInput().split(',').map(String::toInt)

        val numbersSpoken = input
            .dropLast(1)
            .mapIndexed { index, number -> number to index + 1 }
            .toMap()
            .toMutableMap()

        var turn = input.size
        var lastNumber = input.last()

        while (turn < n) {
            if (numbersSpoken.containsKey(lastNumber)) {
                // this number has been spoken before
                val previousTurn = numbersSpoken.getValue(lastNumber)
                numbersSpoken[lastNumber] = turn
                lastNumber = turn - previousTurn
            } else {
                // this was the first time, say 0
                numbersSpoken[lastNumber] = turn
                lastNumber = 0
            }

            turn++
        }

        return lastNumber
    }
}