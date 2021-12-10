package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver
import nl.vloris.adventofcode.common.Point
import nl.vloris.adventofcode.common.points

fun main() = Day9.solve()

object Day9 : BaseSolver(2021, 9) {
    private val map = HeightMap(getInputLines())

    override fun part1(): Int = map.findLowPoints()
        .sumOf { map[it].digitToInt() + 1 }

    override fun part2(): Int = map.findLowPoints()
        .map { lowPoint -> map.findBasin(lowPoint) }
        .sortedDescending()
        .take(3)
        .reduce { total, size -> total * size }
}

private class HeightMap(input: List<String>) {
    val height = input.size + 2
    val width = input[0].length + 2
    val map = listOf("9".repeat(width)) + input.map { "9${it}9" } + listOf("9".repeat(width))

    fun findLowPoints(): Sequence<Point> = (1..width - 2).points(1..height - 2)
        .filter { it.neighbours().none { n -> this[n] <= this[it] } }

    fun findBasin(lowPoint: Point): Int {
        val floodedPoints = mutableSetOf(lowPoint)
        val pointsToVisit = mutableListOf(lowPoint)

        while (pointsToVisit.isNotEmpty()) {
            val currentPoint = pointsToVisit.removeLast()
            val floodedNeighbours = currentPoint.neighbours().filter { this[it] != '9' && it !in floodedPoints }

            floodedPoints.addAll(floodedNeighbours)
            pointsToVisit.addAll(floodedNeighbours)
        }

        return floodedPoints.count()
    }

    operator fun get(p: Point): Char = map[p.y][p.x]
}