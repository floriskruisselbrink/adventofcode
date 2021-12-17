package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver
import nl.vloris.adventofcode.common.Point

fun main() = Day17.solve()

object Day17 : BaseSolver(2021, 17) {
    override fun part1(): Int {
        val targetArea = parse(getInput())

        var maxY = 0
        for (dx in 1..100) {
            for (dy in -100..100) {
                val y = tryProbe(dx, dy, targetArea)
                if (y != null) maxY = maxOf(maxY, y)
            }
        }
        return maxY
    }

    override fun part2(): Int {
        val targetArea = parse(getInput())

        var count = 0
        for (dx in 1..1000) {
            for (dy in -1000..1000) {
                val y = tryProbe(dx, dy, targetArea)
                if (y != null) count++
            }
        }
        return count
    }

    private fun tryProbe(dx: Int, dy: Int, targetArea: Pair<IntRange, IntRange>): Int? {
        val probe = launchProbe(dx, dy)
        val (xRange, yRange) = targetArea

        var maxY = 0

        val hitTarget = probe.takeWhile { (x, y) ->
            maxY = maxOf(y, maxY)
            x <= xRange.last && y >= yRange.first }
            .firstOrNull {
                it.inBound(xRange, yRange)
            }

        return if (hitTarget != null) maxY else null
    }

    private fun launchProbe(dx: Int, dy: Int) = sequence {
        var p = Point(0, 0)
        var d = Point(dx, dy)
        while (true) {
            p += d
            d = Point(if (d.x > 0) d.x-1 else 0, d.y-1)
            yield(p)
        }
    }

    private fun parse(input: String) =
        Regex("""target area: x=(-?\d+)\.\.(-?\d+), y=(-?\d+)\.\.(-?\d+)""")
            .matchEntire(input)!!.let {
                it.groupValues[1].toInt()..it.groupValues[2].toInt() to it.groupValues[3].toInt()..it.groupValues[4].toInt()
            }

}