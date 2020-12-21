package nl.vloris.adventofcode.common

import java.lang.IllegalArgumentException

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)

    fun inBound(xRange: IntRange, yRange: IntRange) = x in xRange && y in yRange

    fun neighbours() = NEIGHBOURS.map { Point(it.x + this.x, it.y + this.y) }

    fun directionTo(other: Point) = when {
        other.x == this.x && other.y < this.y -> Direction.NORTH
        other.x == this.x && other.y > this.y -> Direction.SOUTH
        other.y == this.y && other.x < this.x -> Direction.WEST
        other.y == this.y && other.x > this.x -> Direction.EAST
        else -> throw IllegalArgumentException("No NSEW direction between $this and $other")
    }

    companion object {
        val NEIGHBOURS = listOf(
            Point(-1, 0),
            Point(1, 0),
            Point(0, -1),
            Point(0, 1)
        )
    }
}

fun IntRange.points(yRange: IntRange) = yRange.flatMap { y -> this.map { x -> Point(x, y) } }.asSequence()
fun IntRange.points() = points(this)

fun Collection<Point>.bounds() =
    this.fold(listOf(Int.MAX_VALUE, Int.MAX_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)) { list, point ->
        listOf(
            minOf(list[0], point.x),
            minOf(list[1], point.y),
            maxOf(list[2], point.x),
            maxOf(list[3], point.y)
        )
    }
        .let { (minX, minY, maxX, maxY) -> Point(minX, minY) to Point(maxX, maxY) }