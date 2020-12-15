package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day11.solve()

fun Array<CharArray>.copy() = Array(size) { get(it).clone() }

private val NEIGHBOURS = listOf(
    Pair(-1, -1),
    Pair(-1, 0),
    Pair(-1, 1),
    Pair(0, -1),
    Pair(0, 1),
    Pair(1, -1),
    Pair(1, 0),
    Pair(1, 1)
)

private class Map(val grid: Array<CharArray>, val tolerance: Int) {
    val width = grid[0].size
    val height = grid.size

    fun transformMap(adjacentSeats: (x: Int, y: Int) -> List<Char>): Pair<Map, Boolean> {
        val newMap = this.copy()
        var somethingChanged = false

        for (x in 0 until this.width) {
            for (y in 0 until this.height) {
                when (this[x, y]) {
                    'L' -> if (adjacentSeats(x, y).all { it != '#' }) {
                        newMap[x, y] = '#'
                        somethingChanged = true
                    } else {
                        newMap[x, y] = 'L'
                    }
                    '#' -> if (adjacentSeats(x, y).filter { it == '#' }.count() >= tolerance) {
                        newMap[x, y] = 'L'
                        somethingChanged = true
                    } else {
                        newMap[x, y] = '#'
                    }
                }
            }
        }

        return Pair(newMap, somethingChanged)
    }

    fun countOccurrences(char: Char) = grid.map { line ->
        line.filter { it == char }.count()
    }.sum()

    fun findFirstChair(x: Int, y: Int, dx: Int, dy: Int): Char? {
        var chair: Char?
        var xx = x
        var yy = y

        do {
            xx += dx
            yy += dy
            chair = this[xx, yy]
        } while (chair == '.')

        return chair
    }

    operator fun get(x: Int, y: Int): Char? {
        return if (x < 0 || x >= width || y < 0 || y >= height)
            null
        else
            grid[y][x]
    }

    operator fun set(x: Int, y: Int, value: Char) {
        grid[y][x] = value
    }

    fun copy(): Map = Map(grid.copy(), tolerance)

    override fun toString(): String {
        val sb = StringBuilder()
        for (line in grid) {
            sb.append(line.joinToString(""))
            sb.append('\n')
        }
        return sb.toString()
    }
}

object Day11 : BaseSolver(2020, 11) {
    override fun part1(): Int {
        var map = getMapInput(4)

        fun adjacentTo(x: Int, y: Int) = NEIGHBOURS.mapNotNull { (dx, dy) -> map[x + dx, y + dy] }

        do {
            val (newMap, somethingChanged) = map.transformMap { x, y -> adjacentTo(x, y) }
            map = newMap
        } while (somethingChanged)

        return map.countOccurrences('#')
    }

    override fun part2(): Int {
        var map = getMapInput(5)

        fun adjacentTo(x: Int, y: Int) = NEIGHBOURS.mapNotNull { (dx, dy) -> map.findFirstChair(x, y, dx, dy) }

        do {
            val (newMap, somethingChanged) = map.transformMap { x, y -> adjacentTo(x, y) }
            map = newMap
        } while (somethingChanged)

        return map.countOccurrences('#')
    }

    private fun getMapInput(tolerance: Int) = getInputLines().map { it.toCharArray() }.toTypedArray().let { Map(it, tolerance) }

    private fun getExampleInputLines(): List<String> =
        """L.LL.LL.LL
LLLLLLL.LL
L.L.L..L..
LLLL.LL.LL
L.LL.LL.LL
L.LLLLL.LL
..L.L.....
LLLLLLLLLL
L.LLLLLL.L
L.LLLLL.LL""".lines()
}