package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day4.solve()

object Day4 : BaseSolver(2021, 4) {
    override fun part1(): Int {
        val (numbers, bingoCards) = parseInput(getInput())

        for (i in numbers.indices) {
            val drawnNumbers = numbers.slice(0..i)
            val winningCard = bingoCards.firstOrNull { it.hasBingo(drawnNumbers) }
            if (winningCard != null) {
                return winningCard.score(drawnNumbers)
            }
        }

        return 0
    }

    override fun part2(): Any? {
        val (numbers, bingoCards) = parseInput(getInput())

        var lastWinningCard: BingoCard? = null
        var lastDrawnNumbers: List<Int> = emptyList()

        val remainingCards = bingoCards.toMutableList()
        for (i in numbers.indices) {
            val drawnNumbers = numbers.slice(0..i)
            remainingCards
                .filter { it.hasBingo(drawnNumbers) }
                .map { card ->
                    remainingCards.remove(card)
                    lastWinningCard = card
                    lastDrawnNumbers = drawnNumbers
                }
        }

        return lastWinningCard?.score(lastDrawnNumbers)
    }

    private fun parseInput(input: String): Pair<List<Int>, List<BingoCard>> {
        val splittedInput = input.split("\n\n")

        val numbers = splittedInput
            .first()
            .split(',')
            .map(String::toInt)

        val bingoCards = splittedInput
            .drop(1)
            .map { it.replace('\n', ' ').trim().split(Regex("\\s+")).map(String::toInt) }
            .map(::BingoCard)

        return Pair(numbers, bingoCards)
    }

    private class BingoCard(val numbers: List<Int>) {
        private val rows = numbers.chunked(5).map { it.toTypedArray() }.toTypedArray()
        private val columns = transpose(rows)

        fun hasBingo(drawnNumbers: List<Int>): Boolean {
            for (row in rows) {
                if (drawnNumbers.containsAll(row.asList())) {
                    return true
                }
            }
            for (col in columns) {
                if (drawnNumbers.containsAll(col.asList())) {
                    return true
                }
            }
            return false
        }

        fun score(drawnNumbers: List<Int>): Int =
            numbers.filter { !drawnNumbers.contains(it) }.sum() * drawnNumbers.last()
    }
}

inline fun <reified T> transpose(xs: Array<Array<T>>): Array<Array<T>> {
    val cols = xs[0].size
    val rows = xs.size
    return Array(cols) { j ->
        Array(rows) { i ->
            xs[i][j]
        }
    }
}
