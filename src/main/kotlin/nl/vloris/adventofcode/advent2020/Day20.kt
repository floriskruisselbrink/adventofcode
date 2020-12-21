package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.*

fun main() = Day20.solve()

object Day20 : BaseSolver(2020, 20) {
    private val availableTiles: List<Tile> by lazy {
        getInput()
            .split("\n\n")
            .map { tile ->
                tile.split("\n", limit = 2).let { (title, content) ->
                    Tile(title.substring(5..8).toInt(), Grid(content.split("\n")))
                }
            }
    }

    private fun solvePuzzle(): Map<Point, TilePlacement> {
        fun solve(map: Map<Point, TilePlacement>, remainingTiles: List<Tile>): Map<Point, TilePlacement> {
            if (remainingTiles.isEmpty()) {
                return map
            }

            val openSpots = map.keys.flatMap { it.neighbours() }.filterNot { map.containsKey(it) }

            for (spot in openSpots) {
                val neighbours = spot.neighbours().mapNotNull { map[it] }
                for (tile in remainingTiles) {
                    for (orientation in tile.grids.indices) {
                        if (neighbours.all { tile.grids[orientation].fits(it.grid, spot.directionTo(it.position)) }) {
                            val result =
                                solve(map + (spot to TilePlacement(tile, orientation, spot)), remainingTiles - tile)
                            if (result.isNotEmpty()) {
                                return result
                            }
                        }
                    }
                }
            }

            return emptyMap()
        }

        val puzzle = mapOf(Point(0, 0) to TilePlacement(availableTiles.first(), 0, Point(0, 0)))
        return solve(puzzle, availableTiles.drop(1))
    }

    override fun part1(): Long {
        val solution = solvePuzzle()
        val corners = solution.keys.bounds()
            .let { (min, max) ->
                listOf(
                    min,
                    max,
                    Point(min.x, max.y),
                    Point(max.x, min.y)
                )
            }

        return corners.map { solution.getValue(it).tile.id.toLong() }.reduce { a, b -> a * b }
    }

    override fun part2(): Int {
        val solution = solvePuzzle()
        val (min, max) = solution.keys.bounds()

        val tileWidth = availableTiles.first().grids.first().grid.size
        val puzzleSize = (max.x - min.x + 1) * (tileWidth - 2)

        val puzzleGrid = Array(puzzleSize) { Array(puzzleSize) { ' ' } }

        for (x in min.x..max.x) {
            for (y in min.y..max.y) {
                val grid = solution.getValue(Point(x, y)).grid.grid

                val x0 = (x - min.x) * (tileWidth - 2)
                val y0 = (y - min.y) * (tileWidth - 2)

                grid.drop(1).dropLast(1).forEachIndexed { gy, s ->
                    s.drop(1).dropLast(1).forEachIndexed { gx, c ->
                        puzzleGrid[y0 + gy][x0 + gx] = c
                    }
                }
            }
        }

        val (monsterGrid, points) = Grid(puzzleGrid).rotations().map { grid ->
            grid to grid.grid.indices.points().filter { p ->
                SEA_MONSTER
                    .map { it + p }
                    .all { it.inBound(grid.grid.indices, grid.grid.indices) && grid.grid[it.y][it.x] == '#' }
            }.toList()
        }.first { it.second.isNotEmpty() }

        val monsterRough = points.flatMap { p -> SEA_MONSTER.map { it + p } }.toSet()

        val allRough = monsterGrid.grid.mapIndexed { y, s ->
            s.mapIndexedNotNull { x, c ->
                if (c == '#') Point(x, y) else null
            }
        }
            .flatten()

        return allRough.size - monsterRough.size
    }

    private val SEA_MONSTER = """
                  # 
#    ##    ##    ###
 #  #  #  #  #  #
    """
        .split("\n")
        .filterNot { it.isBlank() }
        .mapIndexed { y, s -> s.mapIndexedNotNull { x, c -> if (c == '#') Point(x, y) else null } }.flatten()


    private data class Tile(val id: Int, val grids: List<Grid>) {
        constructor(id: Int, grid: Grid) : this(id, grid.rotations())
    }

    private data class Grid(val grid: List<String>) {
        constructor(grid: Array<Array<Char>>) : this(grid.map { it.joinToString("") })

        private val edgeNorth = grid.first()
        private val edgeEast = grid.map { it.last() }.joinToString("")
        private val edgeSouth = grid.last()
        private val edgeWest = grid.map { it.first() }.joinToString("")

        fun fits(other: Grid, direction: Direction): Boolean =
            when (direction) {
                Direction.NORTH -> edgeNorth == other.edgeSouth
                Direction.EAST -> edgeEast == other.edgeWest
                Direction.SOUTH -> edgeSouth == other.edgeNorth
                Direction.WEST -> edgeWest == other.edgeEast
            }

        fun flipVertical(): Grid = Grid(grid.reversed())
        fun flipHorizontal(): Grid = Grid(grid.map { it.reversed() })
        fun rotate(times: Int): Grid {
            var input = grid
            val array = Array(input.size) { Array(input.size) { '?' } }

            repeat(times) {
                input.indices.points().forEach { (x, y) ->
                    array[y][x] = input[input.size - x - 1][y]
                }
                input = array.map { it.joinToString("") }
            }

            return Grid(input)
        }

        fun rotations() = setOf(
            this,
            rotate(1),
            rotate(2),
            rotate(3),
            flipHorizontal(),
            flipVertical(),
            rotate(1).flipHorizontal(),
            rotate(1).flipVertical()
        ).toList()

        override fun toString(): String {
            return grid.joinToString("\n")
        }
    }

    private data class TilePlacement(val tile: Tile, val rotation: Int, val position: Point) {
        val grid = tile.grids[rotation]
    }
}