package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver
import nl.vloris.adventofcode.common.Direction
import nl.vloris.adventofcode.common.Point
import kotlin.math.abs

fun main() = Day12.solve()

private val DIRECTIONS = listOf('E', 'S', 'W', 'N')

private class Ship {
    var location = Point(0, 0)
    var facing = 0

    fun move(direction: Char, length: Int) {
        when (direction) {
            'F' -> move(DIRECTIONS[facing], length)
            'L' -> facing = (facing + 4 - (length / 90)).rem(DIRECTIONS.size)
            'R' -> facing = (facing + (length / 90)).rem(DIRECTIONS.size)
            in (DIRECTIONS) -> location = location.plus(Direction.from(direction), length)
            else -> throw IllegalArgumentException()
        }
    }
}

object Day12 : BaseSolver(2020, 12) {
    override fun part1(): Int {
        val ship = Ship()
        for (line in getInputLines()) {
            val direction = line[0]
            val length = line.drop(1).toInt()

            ship.move(direction, length)
        }

        return abs(ship.location.x) + abs(ship.location.y)
    }

    override fun part2(): Int {
        var ship = Point(0, 0)
        var waypoint = Point(10, -1)

        for (line in getInputLines()) {
            val direction = line[0]
            val length = line.drop(1).toInt()

            when (direction) {
                in (DIRECTIONS) -> waypoint = waypoint.plus(Direction.from(direction), length)
                'L' -> waypoint = waypoint.rotate(-1 * length)
                'R' -> waypoint = waypoint.rotate(length)
                'F' -> ship += (waypoint * length)
                else -> throw IllegalArgumentException()
            }
        }

        return abs(ship.x) + abs(ship.y)
    }


    fun getDummyInputLines(): List<String> =
        """F10
N3
F7
L270
F11""".lines()
}