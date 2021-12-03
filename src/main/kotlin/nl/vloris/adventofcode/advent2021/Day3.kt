package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day3.solve()

object Day3 : BaseSolver(2021, 3) {
    private val mostSignificantBit = getInputLines().first().length - 1
    private val lengthMask = Integer.parseInt("1".repeat(mostSignificantBit + 1), 2)
    private val diagnostics: List<Int> by lazy {
        getInputLines().map { Integer.parseInt(it, 2) }
    }

    override fun part1(): Int {
        var gammaRate = 0
        for (n in 0..mostSignificantBit) {
            val bitmask = 1 shl n
            val countOne = diagnostics.count { (it and bitmask) != 0 }
            val countZero = diagnostics.size - countOne

            if (countOne > countZero) {
                gammaRate = gammaRate or bitmask
            }
        }

        val epsilonRate = gammaRate.inv() and lengthMask

        return gammaRate * epsilonRate
    }

    override fun part2(): Int {
        val oxygenRating = findRating { ones, zeroes -> ones >= zeroes}
        val co2Rating = findRating { ones, zeroes -> ones < zeroes}
        return oxygenRating * co2Rating
    }

    private fun findRating(filter: (Int, Int) -> Boolean): Int {
        val results = diagnostics.toMutableList()
        var bitmask = 1 shl mostSignificantBit
        while (results.size > 1) {
            val countOne = results.count { (it and bitmask) != 0 }
            val countZero = results.size - countOne

            if (filter(countOne, countZero)) {
                results.removeIf { (it and bitmask) == 0 }
            } else {
                results.removeIf { (it and bitmask) != -0 }
            }

            bitmask = bitmask shr 1
        }

        return results.first()
    }
}