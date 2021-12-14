package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day14.solve()

object Day14 : BaseSolver(2021, 14) {
    override fun part1(): Long {
        val (polymer, mapping) = parseInput(getInput())
        return solve(polymer, mapping, 10)
    }

    override fun part2(): Long {
        val (polymer, mapping) = parseInput(getInput())
        return solve(polymer, mapping, 40)
    }

    private fun solve(polymer: Polymer, mapping: Map<String, List<String>>, steps: Int): Long {
        var newPolymer = polymer

        repeat(steps) { newPolymer = newPolymer.polymerize(mapping) }

        val elements = newPolymer.countElements()

        return elements.values.maxOrNull()!! - elements.values.minOrNull()!!
    }

    private fun parseInput(input: String): Pair<Polymer, Map<String, List<String>>> {
        val (startSequence, pairs) = input.split("\n\n")

        val pairCounts = startSequence.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }
        val mapping = pairs.split('\n').associate { line ->
            val (pair, insert) = line.split(" -> ")
            pair to listOf("${pair[0]}${insert}", "${insert}${pair[1]}")
        }

        return Polymer(startSequence.first(), startSequence.last(), pairCounts) to mapping
    }

    private data class Polymer(val firstElement: Char, val lastElement: Char, val pairs: Map<String, Long>) {
        fun polymerize(mapping: Map<String, List<String>>): Polymer {
            val newPairs = mutableMapOf<String, Long>()
            pairs.forEach { (pair, count) ->
                mapping.getValue(pair).forEach { m ->
                    newPairs[m] = newPairs.getOrDefault(m, 0) + count
                }
            }
            return this.copy(pairs = newPairs)
        }

        fun countElements(): Map<Char, Long> {
            return pairs.flatMap { (pair, count) -> listOf(pair[0] to count, pair[1] to count) }
                .groupingBy { it.first }
                .fold(0L) { sum, (_, count) -> sum + count }
                .mapValues { (element, count) ->
                    if (element == firstElement || element == lastElement)
                        (count + 1) / 2
                    else
                        count / 2
                }
        }
    }
}
