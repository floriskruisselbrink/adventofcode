package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day20.solve()

object Day20 : BaseSolver(2021, 20) {
    override fun part1(): Int {
        val (algorithm, image) = parse(getInput())
        return solve(algorithm, image, 2)
    }

    override fun part2(): Int {
        val (algorithm, image) = parse(getInput())
        return solve(algorithm, image, 50)
    }

    private fun solve(algorithm: String, image: Image, times: Int): Int {
        return generateSequence(image) { enhancedImage ->
            enhancedImage.enhance(algorithm)
        }.elementAt(times).litPixels()
    }

    private data class Image(val grid: List<String>, val background: Char = '.') {
        private val width = grid.first().length
        private val height = grid.size

        operator fun get(x: Int, y: Int): Char = if (x in 0 until width && y in 0 until height) {
            grid[y][x]
        } else {
            background
        }

        fun getPixelCode(x: Int, y: Int): Int = listOf(
            get(x - 1, y - 1),
            get(x, y - 1),
            get(x + 1, y - 1),
            get(x - 1, y),
            get(x, y),
            get(x + 1, y),
            get(x - 1, y + 1),
            get(x, y + 1),
            get(x + 1, y + 1)
        ).map { if (it == '#') '1' else '0' }.joinToString("").toInt(2)

        fun enhance(algorithm: String) = Image(
            (-1..height).map { y ->
                (-1..width).map { x ->
                    algorithm[getPixelCode(x, y)]
                }.joinToString("")
            }, if (background == '#') algorithm.last() else algorithm.first()
        )

        fun litPixels(): Int = grid.sumOf { line -> line.count { it == '#' } }

        override fun toString(): String = grid.joinToString("\n")
    }

    private fun parse(input: String): Pair<String, Image> {
        val (algorithm, image) = input.split("\n\n")
        return algorithm to Image(image.lines())
    }
}