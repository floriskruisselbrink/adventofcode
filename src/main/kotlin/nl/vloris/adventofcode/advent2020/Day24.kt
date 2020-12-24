package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day24.solve()

private enum class DirectionHex(val instruction: String) {
    EAST("e"),
    SOUTHEAST("se"),
    SOUTHWEST("sw"),
    WEST("w"),
    NORTHWEST("nw"),
    NORTHEAST("ne");

    companion object {
        private val map = values().associateBy { it.instruction }
        fun from(instruction: String) =
            map[instruction] ?: throw IllegalArgumentException("Unknown instruction $instruction")
    }
}

private data class PointHex(val x: Int, val y: Int, val z: Int) {
    operator fun plus(other: PointHex) = PointHex(x + other.x, y + other.y, z + other.z)
    operator fun plus(direction: DirectionHex) = when (direction) {
        DirectionHex.EAST -> PointHex(x + 1, y - 1, z)
        DirectionHex.SOUTHEAST -> PointHex(x, y - 1, z + 1)
        DirectionHex.SOUTHWEST -> PointHex(x - 1, y, z + 1)
        DirectionHex.WEST -> PointHex(x - 1, y + 1, z)
        DirectionHex.NORTHWEST -> PointHex(x, y + 1, z - 1)
        DirectionHex.NORTHEAST -> PointHex(x + 1, y, z - 1)
    }

    fun neighbours() = NEIGHBOURS.map { PointHex(it.x + this.x, it.y + this.y, it.z + this.z) }

    companion object {
        val NEIGHBOURS = listOf(
            PointHex(1, -1, 0),
            PointHex(0, -1, 1),
            PointHex(-1, 0, 1),
            PointHex(-1, 1, 0),
            PointHex(0, 1, -1),
            PointHex(1, 0, -1)
        )
    }
}

object Day24 : BaseSolver(2020, 24) {
    override fun part1(): Int =
        getInputLines().map(::parseInstruction).let(::parseInstructions).size

    override fun part2(): Int {
        var grid = getInputLines().map(::parseInstruction).let(::parseInstructions)

        repeat(100) { grid = nextIteration(grid) }

        return grid.size
    }

    private fun parseInstruction(instruction: String) = sequence {
        val regex = """[ns]?[ew]""".toRegex()
        var match = regex.find(instruction)
        while (match != null) {
            yield(DirectionHex.from(match.value))
            match = match.next()
        }
    }

    private fun parseInstructions(instructions: List<Sequence<DirectionHex>>): Set<PointHex> {
        val grid = mutableSetOf<PointHex>()
        instructions.forEach { instructionSet ->
            val point = instructionSet.fold(PointHex(0, 0, 0)) { p, d -> p + d }
            if (!grid.remove(point)) {
                grid.add(point)
            }
        }

        return grid
    }

    private fun nextIteration(grid: Set<PointHex>): Set<PointHex> {
        val nextGrid = grid.toMutableSet()

        // find all black tiles with zero or more than 2 black neighbours
        grid.filter { tile ->
            val blackNeighbours = tile.neighbours().filter { grid.contains(it) }.size
            blackNeighbours == 0 || blackNeighbours > 2
        }.let {
            nextGrid.removeAll(it)
        }

        grid
            // find all white tiles adjacent to one or more black tiles
            .asSequence()
            .map { it.neighbours() }
            .flatten().toSet()
            .filterNot { grid.contains(it) }
            // filter only white tiles with exactly 2 black neighbours
            .filter { tile ->
                val blackNeighbours = tile.neighbours().filter { grid.contains(it) }.size
                blackNeighbours == 2
            }
            .toList()
            // make them black
            .let {
                nextGrid.addAll(it)
            }

        return nextGrid
    }
}