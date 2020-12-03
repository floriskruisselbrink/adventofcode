package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

class Day3 : BaseSolver(2020, 3) {
    private val treeMap = getInputLines()

    override fun part1() {
        val treesFound = countTreesOnSlope(treeMap, Point(3, 1))
        println("$treesFound trees encountered")
    }

    override fun part2() {
        val result =
                    countTreesOnSlope(treeMap, Point(1, 1)) *
                    countTreesOnSlope(treeMap, Point(3, 1)) *
                    countTreesOnSlope(treeMap, Point(5, 1)) *
                    countTreesOnSlope(treeMap, Point(7, 1)) *
                    countTreesOnSlope(treeMap, Point(1, 2))
        println(result)
    }

    private fun countTreesOnSlope(treeMap: List<String>, slope: Point): Int {
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

data class Point(val x: Int, val y: Int)