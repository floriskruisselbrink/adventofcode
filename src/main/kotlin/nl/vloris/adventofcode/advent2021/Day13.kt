package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver
import nl.vloris.adventofcode.common.Point
import nl.vloris.adventofcode.common.bounds
import kotlin.math.abs

fun main() = Day13.solve()

object Day13 : BaseSolver(2021, 13) {
    override fun part1(): Int {
        val (points, instructions) = parse(getInput())
        val result = foldPaper(points, instructions.take(1))
        return result.size
    }

    override fun part2(): String {
        val (points, instructions) = parse(getInput())
        val result = foldPaper(points, instructions)
        return prettyPrint(result)
    }

    private fun foldPaper(points: Set<Point>, folds: List<Pair<String, Int>>) =
        folds.fold(points) { result, (axis, fold) ->
            if (axis == "x")
                result.map { it.foldX(fold) }.toSet()
            else
                result.map { it.foldY(fold) }.toSet()
        }

    private fun prettyPrint(points: Set<Point>): String {
        val bounds = points.bounds()
        return (bounds.first.y..bounds.second.y).joinToString(separator = "\n", prefix = "\n") { y ->
            (bounds.first.x..bounds.second.x).joinToString("") { x ->
                if (points.contains(Point(x, y))) "#" else "."
            }
        }
    }

    private fun Point.foldX(x: Int): Point = Point(x - abs(this.x - x), this.y)
    private fun Point.foldY(y: Int): Point = Point(this.x, y - abs(this.y - y))

    private fun parse(input: String): Pair<Set<Point>, List<Pair<String, Int>>> {
        val (points, instructions) = input.split("\n\n")
        val regex = """fold along ([xy])=(\d+)""".toRegex()
        return points.lines().map(::Point).toSet() to instructions.lines().map {
            val (axis, fold) = regex.matchEntire(it)!!.destructured
            axis to fold.toInt()
        }
    }
}