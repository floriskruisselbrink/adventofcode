package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver
import nl.vloris.adventofcode.common.Point

fun main() = Day3.solve()

object Day3 : BaseSolver(2020, 3) {
    private val treeMap = getInputLines()

    override fun part1() = countTreesOnSlope(Point(3, 1))

    override fun part2() =
                countTreesOnSlope(Point(1, 1)) *
                countTreesOnSlope(Point(3, 1)) *
                countTreesOnSlope(Point(5, 1)) *
                countTreesOnSlope(Point(7, 1)) *
                countTreesOnSlope(Point(1, 2))

    private fun countTreesOnSlope(slope: Point): Int {
        val mapWidth = treeMap[0].length
        val mapHeight = treeMap.size

        var location = Point(0, 0)

        var treesFound = 0
        while (location.y < mapHeight) {
            if (treeMap[location.y][location.x] == '#') {
                treesFound++
            }
            location = Point((location.x + slope.x).rem(mapWidth), location.y + slope.y)
        }

        return treesFound
    }

}

