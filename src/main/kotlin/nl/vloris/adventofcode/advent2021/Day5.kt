package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver
import nl.vloris.adventofcode.common.Point
import kotlin.math.sign

fun main() = Day5.solve()

object Day5 : BaseSolver(2021, 5) {
    override fun part1(): Int = solve(input.filter(Vector::isStraight))
    override fun part2(): Int = solve(input)

    private val input: List<Vector> by lazy {
        getInputLines().map {
            val (first, second) = it.split(" -> ").map(::Point)
            Vector(first, second)
        }
    }

    private data class Vector(val from: Point, val to: Point) {
        fun isStraight(): Boolean =
            from.x == to.x || from.y == to.y

        fun allPoints(): Sequence<Point> {
            val stepX = (to.x - from.x).sign
            val stepY = (to.y - from.y).sign

            return sequence {
                var curX = from.x
                var curY = from.y
                while (curX != to.x || curY != to.y) {
                    yield(Point(curX, curY))
                    curX += stepX
                    curY += stepY
                }
                yield(Point(curX, curY))
            }
        }
    }

    private fun solve(vents: List<Vector>): Int {
        val coverage = mutableMapOf<Point, Int>().withDefault { 0 }
        vents.forEach { vent ->
            vent.allPoints().forEach { point ->
                val currentCoverage = coverage.getOrDefault(point, 0)
                coverage[point] = currentCoverage + 1
            }
        }

        return coverage.filterValues { it > 1 }.count()
    }
}