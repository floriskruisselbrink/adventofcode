package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day23.solve()

object Day23 : BaseSolver(2020, 23) {
    override fun part1(): String {
        val cups = CupsGame(getInput(), getInput().length)
        cups.play(100)
        return cups.result1()
    }

    override fun part2(): Long {
        val cups = CupsGame(getInput(), 1_000_000)
        cups.play(10_000_000)
        return cups.result2()
    }

    private class CupsGame(input: String, val largestCup: Int) {
        private val startingCups: List<Int> = input.map(Character::getNumericValue) + when {
            largestCup > input.length -> IntRange(input.length + 1, largestCup).toList()
            else -> emptyList()
        }
        private val nextCup: IntArray = IntArray(startingCups.size + 2) { 0 }
        private var currentCup: Int = startingCups[0]

        init {
            startingCups.forEachIndexed { i, c ->
                nextCup[c] = startingCups[(i + 1) % startingCups.size]
            }
        }

        fun play(moves: Int) {
            for (move in 1..moves) {
                val pickedUp = pickCups()
                val destination = destination(pickedUp)
                reorderCups(pickedUp, destination)
                currentCup = nextCup[currentCup]
            }
        }

        private fun pickCups(): List<Int> = listOf(
            nextCup[currentCup],
            nextCup[nextCup[currentCup]],
            nextCup[nextCup[nextCup[currentCup]]],
        )

        private fun destination(pickedUp: List<Int>): Int {
            var dest = currentCup - 1
            while (pickedUp.contains(dest) || dest < 1) {
                dest--
                if (dest < 1) dest = largestCup
            }
            return dest
        }

        private fun reorderCups(pickedUp: List<Int>, destination: Int) {
            nextCup[currentCup] = nextCup[pickedUp[2]]
            val oldNext = nextCup[destination]
            nextCup[destination] = pickedUp[0]
            nextCup[pickedUp[2]] = oldNext
        }

        fun result1(): String {
            return cups(1).joinToString("")
        }

        fun result2(): Long {
            return cups(1)
                .take(2)
                .map(Int::toLong).reduce { a, b -> a * b }
        }

        fun cups(startAfter: Int) = sequence {
            var i = startAfter
            while (nextCup[i] != startAfter) {
                i = nextCup[i]
                yield(i)
            }
        }
    }
}