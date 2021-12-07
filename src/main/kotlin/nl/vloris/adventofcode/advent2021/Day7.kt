package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver
import kotlin.math.abs

fun main() = Day7.solve()

object Day7 : BaseSolver(2021, 7) {
    private val crabs: List<Int> by lazy {
        getInput().split(',').map(String::toInt).sorted()
    }

    override fun part1(): Int = solve { distance -> distance }
    override fun part2(): Int = solve { distance -> (distance * (distance + 1)) / 2 }

    private fun solve(calculateFuel: (Int) -> Int): Int {
        val smallest = crabs.first()
        val largest = crabs.last()
        val distances = Array(crabs.size) { -1 }

        var leastFuelUsed = Int.MAX_VALUE
        for (targetPosition in smallest..largest) {
            for (i in crabs.indices) {
                distances[i] = calculateFuel(abs(crabs[i] - targetPosition))
            }

            val fuelUsed = distances.sum()
            if (fuelUsed < leastFuelUsed) {
                leastFuelUsed = fuelUsed
            }
        }

        return leastFuelUsed
    }
}