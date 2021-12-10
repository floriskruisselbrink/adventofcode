package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day10.solve()

object Day10 : BaseSolver(2021, 10) {
    override fun part1(): Int = getInputLines()
        .map { parseInput(it) }
        .filterIsInstance<ParseResult.SyntaxError>()
        .sumOf { it.score }

    override fun part2(): Long = getInputLines()
        .map { parseInput(it) }
        .filterIsInstance<ParseResult.Incomplete>()
        .map { it.score }
        .sorted()
        .middle()

    private const val OPENING_BRACES = "([{<"
    private val BRACE_PAIRS = listOf("()", "[]", "{}", "<>")

    private fun parseInput(input: String): ParseResult {
        val context = mutableListOf<Char>()
        input.forEach { currentChar ->
            if (currentChar in OPENING_BRACES) {
                context.add(currentChar)
            } else {
                val lastChar = context.removeLast()
                if ("${lastChar}${currentChar}" !in BRACE_PAIRS)
                    return ParseResult.SyntaxError(currentChar)
            }
        }

        return context.foldRight(0L) { currentChar, score ->
            when (currentChar) {
                '(' -> score * 5 + 1
                '[' -> score * 5 + 2
                '{' -> score * 5 + 3
                '<' -> score * 5 + 4
                else -> throw IllegalArgumentException()
            }
        }.let { ParseResult.Incomplete(it) }
    }
}

private sealed class ParseResult {
    data class SyntaxError(val wrongCharacter: Char) : ParseResult() {
        val score: Int = when (wrongCharacter) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> throw IllegalArgumentException()
        }
    }

    data class Incomplete(val score: Long) : ParseResult()
}

private fun <T> List<T>.middle(): T = this[this.size / 2]
