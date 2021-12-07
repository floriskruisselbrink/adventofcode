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

    private fun solve(calculateFuel: (Int) -> Int) =
        (crabs.first()..crabs.last()).map { targetPosition ->
            crabs.sumOf { crabPosition ->
                calculateFuel(abs(targetPosition - crabPosition))
            }
        }.minOf { it }
}