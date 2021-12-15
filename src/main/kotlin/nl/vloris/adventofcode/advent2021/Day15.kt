package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver
import nl.vloris.adventofcode.common.Point
import nl.vloris.adventofcode.common.points
import java.util.*

fun main() = Day15.solve()

object Day15 : BaseSolver(2021, 15) {
    override fun part1(): Int =
        Grid(getInputLines()).findShortestPathDijkstra()
    
    override fun part2(): Int =
        Grid(getInputLines()).expand(5, 5).findShortestPathDijkstraPriority()

    private class Grid(val grid: Array<IntArray>) {
        constructor(input: List<String>) : this(input.map { it.map(Char::digitToInt).toIntArray() }.toTypedArray())

        val start = Point(0, 0)
        val finish = Point(grid[0].size - 1, grid.size - 1)

        val width = 0..finish.x
        val height = 0..finish.y

        fun findShortestPathDijkstra(): Int {
            val queue = grid.indices.points().toMutableSet()
            val distance = mutableMapOf<Point, Int>()

            distance[start] = 0

            while (queue.isNotEmpty()) {
                val u = distance.filterKeys { it in queue }.minByOrNull { it.value }!!.key
                queue.remove(u)

                if (u == finish) break

                val distanceU = distance.getValue(u)
                u.neighbours().filter { it in queue }.forEach { v ->
                    val alt = distanceU + this[v]
                    if (alt < distance.getOrDefault(v, Int.MAX_VALUE)) {
                        distance[v] = alt
                    }
                }
            }

            return distance[finish]!!
        }

        private data class Node(val value: Point, val priority: Int)

        fun findShortestPathDijkstraPriority(): Int {
            val queue = PriorityQueue(compareBy(Node::priority))
            val visited = mutableSetOf<Point>()
            val distance = mutableMapOf<Point, Int>()

            distance[start] = 0
            queue.add(Node(start, 0))

            while (queue.isNotEmpty()) {
                val (current, _) = queue.remove()
                if (current == finish) break

                visited.add(current)

                val distanceU = distance.getValue(current)
                current.neighbours().filter { it.inBound(width, height) && it !in visited }.forEach { neighbour ->
                    val alt = distanceU + this[neighbour]
                    if (alt < distance.getOrDefault(neighbour, Int.MAX_VALUE)) {
                        distance[neighbour] = alt
                        queue.add(Node(neighbour, alt))
                    }
                }
            }

            return distance[finish]!!
        }

        fun expand(width: Int, height: Int): Grid {
            val wideGrid = grid.map { row ->
                var wideRow = row
                var transformed = transformRow(row)
                repeat(width - 1) {
                    wideRow += transformed
                    transformed = transformRow(transformed)
                }

                wideRow
            }.toTypedArray()

            var highGrid = wideGrid
            var transformed = transform(wideGrid)
            repeat(height - 1) {
                highGrid += transformed
                transformed = transform(transformed)
            }
            return Grid(highGrid)
        }

        private fun transformRow(row: IntArray) = IntArray(row.size) { row[it] % 9 + 1 }
        private fun transform(grid: Array<IntArray>): Array<IntArray> = Array(grid.size) { y ->
            IntArray(grid[0].size) { x -> grid[y][x] % 9 + 1 }
        }

        operator fun get(p: Point): Int = grid[p.y][p.x]

        override fun toString(): String = grid.joinToString("\n") { it.joinToString("") }
    }
}
