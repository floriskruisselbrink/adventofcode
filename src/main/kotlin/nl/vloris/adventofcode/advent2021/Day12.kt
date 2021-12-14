package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day12.solve()

object Day12 : BaseSolver(2021, 12) {
    override fun part1(): Int = solve(getInput(), false)
    override fun part2(): Int = solve(getInput(), true)

    private fun solve(input: String, allowOneExtraVisit: Boolean): Int {
        val graph = parse(input)
        val paths = mutableListOf<List<Cave>>()
        graph.dfs("start", "end", allowOneExtraVisit) { paths.add(it) }
        return paths.size
    }

    private fun parse(input: String): Graph =
        input.lineSequence().map { it.substringBefore('-') to it.substringAfter('-') }
            .flatMap { (a, b) -> listOf(a to b, b to a) }
            .groupBy({ it.first }, { it.second })
            .let { Graph(it) }
}

private typealias Cave = String

private class Graph(val adjacencyMap: Map<Cave, List<Cave>>) {

    fun dfs(start: Cave, end: Cave, allowOneExtraVisit: Boolean, handler: (path: List<Cave>) -> Unit) {
        fun checkExtraVisit(cave: Cave, path: List<Cave>) = if (allowOneExtraVisit) {
            path.filter { it.lowercase() == it }.groupingBy { it }.eachCount().any { it.value >= 2 }
        } else {
            true
        }

        fun dfs(from: Cave, to: Cave, path: MutableList<Cave>) {
            path.add(from)

            if (from == to) {
                handler(path.toList())
            } else {
                adjacencyMap[from]
                    ?.filterNot { it == "start" || (it.lowercase() == it) && it in path && checkExtraVisit(it, path) }
                    ?.forEach { dfs(it, to, path) }
            }

            path.removeLast()
        }

        dfs(start, end, mutableListOf())
    }

}