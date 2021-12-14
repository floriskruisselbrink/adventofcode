package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver
import nl.vloris.adventofcode.common.Point

fun main() = Day11.solve()

object Day11 : BaseSolver(2021, 11) {
    override fun part1(): Int {
        val input = Grid(parse(getInput()))

        return generateSequence(input) { it.chargeEnergy() }
            .drop(1)
            .take(100)
            .sumOf { it.flashed() }
    }

    override fun part2(): Int {
        val input = Grid(parse(getInput()))

        return generateSequence(input) { it.chargeEnergy() }
            .map { it.flashed() }
            .indexOfFirst { it == 100 }
    }

    private fun parse(input: String) = input.lineSequence().flatMapIndexed { y, line ->
        line.mapIndexed { x, char -> Point(x, y) to char.digitToInt() }
    }.toMap()

    private class Grid(val octopuses: Map<Point, Int>) {
        fun flashed(): Int = octopuses.count { it.value == 0 }

        fun chargeEnergy(): Grid {
            var increased = Grid(octopuses.mapValues { it.value + 1 })
            while (increased.isFlashing()) {
                increased = increased.flashStep()
            }
            return increased
        }

        private fun flashStep(): Grid =
            Grid(octopuses.mapValues { (pos, value) ->
                if (value in 1..9) value + pos.allNeighbours().count { octopuses.getOrDefault(it, 0) > 9 }
                else 0
            })

        private fun isFlashing(): Boolean = octopuses.any { it.value > 9 }
    }

    private fun getTestInput() = """5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526"""
}