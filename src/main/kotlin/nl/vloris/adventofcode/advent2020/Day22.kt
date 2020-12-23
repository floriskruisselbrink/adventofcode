package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day22.solve()

private typealias Deck = List<Int>

private typealias GameResult = Pair<Int, Deck>

private enum class GameMode { SIMPLE, RECURSIVE }

object Day22 : BaseSolver(2020, 22) {
    override fun part1() = playGame(parseInput(), GameMode.SIMPLE).let(::score)

    override fun part2() = playGame(parseInput(), GameMode.RECURSIVE).let(::score)

    private fun score(result: GameResult): Int =
        result.second.mapIndexed { i, v -> (result.second.size - i) * v }.sum()

    private fun playGame(players: Pair<Deck, Deck>, mode: GameMode): GameResult {
        val history = mutableSetOf<Pair<Deck, Deck>>()

        tailrec fun play(player1: MutableList<Int>, player2: MutableList<Int>): GameResult {
            if (mode == GameMode.RECURSIVE) {
                if (history.contains(player1 to player2)) {
                    return 1 to player1
                } else {
                    history.add(player1 to player2)
                }
            }

            val (card1, card2) = player1.removeFirst() to player2.removeFirst()
            val winner = when {
                mode == GameMode.RECURSIVE && player1.size >= card1 && player2.size >= card2 ->
                    playGame(
                        player1.take(card1) to player2.take(card2), mode
                    ).first
                (card1 > card2) -> 1
                else -> 2
            }

            when (winner) {
                1 -> with(player1) {
                    add(card1)
                    add(card2)
                }
                2 -> with(player2) {
                    add(card2)
                    add(card1)
                }
            }

            return when {
                player1.isEmpty() -> 2 to player2
                player2.isEmpty() -> 1 to player1
                else -> play(player1, player2)
            }
        }

        return play(players.first.toMutableList(), players.second.toMutableList())
    }

    private fun parseInput(): Pair<Deck, Deck> =
        getInput()
            .split("\n\n")
            .map { deck -> deck.split("\n").drop(1).map(String::toInt) }
            .zipWithNext()
            .first()
}