package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day8.solve()

private typealias EncodedNumber = Set<Char>

private data class EncodingNotes(val input: List<EncodedNumber>, val output: List<EncodedNumber>)

object Day8 : BaseSolver(2021, 8) {
    private val SIMPLE_NUMBER_LENGTHS = listOf(2, 4, 3, 7)
    override fun part1(): Int = parseInput().flatMap { it.output }.count { it.size in SIMPLE_NUMBER_LENGTHS }
    override fun part2(): Int = parseInput().sumOf { it.solve() }

    private fun EncodingNotes.solve(): Int {
        val one = input.single { it.size == 2 }
        val four = input.single { it.size == 4 }
        val seven = input.single { it.size == 3 }
        val eight = input.single { it.size == 7 }

        val segmentB = ('a'..'g').single { segment -> input.count { segment in it } == 6 }
        val segmentE = ('a'..'g').single { segment -> input.count { segment in it } == 4 }
        val segmentF = ('a'..'g').single { segment -> input.count { segment in it } == 9 }
        val segmentC = one.single { it != segmentF }

        val lengthFive = input.filter { it.size == 5 }
        val two = lengthFive.single { segmentF !in it }
        val five = lengthFive.single { segmentB in it }
        val three = lengthFive.single { it != two && it != five }

        val lengthSix = input.filter { it.size == 6 }
        val six = lengthSix.single { segmentC !in it }
        val nine = lengthSix.single { segmentE !in it }
        val zero = lengthSix.single {it != six && it != nine }

        val mapping = listOf(zero, one, two, three, four, five, six, seven, eight, nine)
        return output.map { mapping.indexOf(it) }.joinToString("").toInt()
    }

    private fun parseInput(): List<EncodingNotes> =
        getInputLines().map { line ->
            val (input, output) = line.split(" | ")
            EncodingNotes(
                input.split(" ").map { it.toSortedSet() },
                output.split(" ").map { it.toSortedSet() }
            )
        }
}