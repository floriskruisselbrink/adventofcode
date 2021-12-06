package nl.vloris.adventofcode.common

import java.lang.IllegalArgumentException
import kotlin.math.*

data class Point(val x: Int, val y: Int) {
    constructor(s: String) : this(s.substringBefore(',').toInt(), s.substringAfter(',').toInt())

    override fun toString(): String {
        return "(${x},${y})"
    }
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun plus(direction: Direction) = plus(direction, 1)

    fun plus(direction: Direction, amount: Int) = when (direction) {
        Direction.NORTH -> Point(x, y - amount)
        Direction.EAST -> Point(x + amount, y)
        Direction.SOUTH -> Point(x, y + amount)
        Direction.WEST -> Point(x - amount, y)
    }

    operator fun times(amount: Int) = Point(x * amount, y * amount)

    fun rotate(degrees: Int): Point = rotate(degrees * (Math.PI / 180.0))
    fun rotate(radials: Double): Point {
        val s = sin(radials)
        val c = cos(radials)

        val nx = x * c - y * s
        val ny = x * s + y * c

        return Point(nx.roundToInt(), ny.roundToInt())
    }

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