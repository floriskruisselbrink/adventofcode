package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day2.solve()

object Day2 : BaseSolver(2021, 2) {
    private val instructions: List<Pair<String, Int>> by lazy {
        getInputLines().map { it.split(' ').let { (direction, count) -> direction to count.toInt() } }
    }

    override fun part1(): Int {
        var depth = 0
        var forward = 0

        instructions.forEach { (direction, count) ->
            when (direction) {
                "forward" -> forward += count
                "down" -> depth += count
                "up" -> depth -= count
                else -> throw IllegalArgumentException("Direction $direction does not exist")
            }
        }

        return depth * forward
    }

    override fun part2(): Int {
        var depth = 0
        var forward = 0
        var aim = 0

        instructions.forEach { (direction, count) ->
            when (direction) {
                "forward" -> {
                    forward += count; depth += aim * count
                }
                "down" -> aim += count
                "up" -> aim -= count
                else -> throw IllegalArgumentException("Direction $direction does not exist")
            }
        }

        return depth * forward
    }
}