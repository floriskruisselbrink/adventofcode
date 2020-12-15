package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day10.solve()

object Day10 : BaseSolver(2020, 10) {
    private val input: List<Int> by lazy { getInputLines().map(String::toInt) }

    override fun part1(): Int {
        val adapters = input.sorted()

        var diff1 = 0
        var diff3 = 1 // include the final device's own adapter
        var previousAdapter = 0

        for (currentAdapter in adapters) {
            when (currentAdapter - previousAdapter) {
                1 -> diff1++
                3 -> diff3++
                else -> throw IllegalArgumentException("Sorry, ik snap alleen 1 en 3")
            }
            previousAdapter = currentAdapter
        }

        return diff1 * diff3
    }

    override fun part2(): Long? {
        val adapters = (listOf(0) + input).sorted()
        val device = adapters.last() + 3

        val possibilities = mutableMapOf<Int, Long>()

        possibilities[device] = 1

        for (i in adapters.reversed()) {
            possibilities[i] =
                possibilities.getOrDefault(i + 1, 0) +
                        possibilities.getOrDefault(i + 2, 0) +
                        possibilities.getOrDefault(i + 3, 0)
        }

        return possibilities[0]
    }

    private fun getDummyInputLines(): List<String> = """28
33
18
42
31
14
46
20
48
47
24
23
49
45
19
38
39
11
1
32
25
35
8
17
7
9
4
2
34
10
3""".lines()
}