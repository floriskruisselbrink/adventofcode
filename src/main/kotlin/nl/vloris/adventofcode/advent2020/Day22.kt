package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day22.solve()

object Day22 : BaseSolver(2020, 22) {
    val debug = false

    private enum class GameMode { SIMPLE, RECURSIVE }

    private class Game(
        player1: List<Int>,
        player2: List<Int>,
        val gameMode: GameMode = GameMode.SIMPLE,
        val game: Int = 1
    ) {
        val decks = listOf(player1.toMutableList(), player2.toMutableList())
        val deckHistory = listOf(mutableListOf<List<Int>>(), mutableListOf<List<Int>>())
        var round = 0

        fun playUntilWinner(): Int {
            while (!hasWinner()) {
                playRound()
            }

            return if (decks[0].isNotEmpty()) 0 else 1
        }

        fun playRound() {
            round++
            if (debug) {
                println("-- Round $round (game $game) --")
                println("Player 1's deck: ${decks[0]}")
                println("Player 2's deck: ${decks[1]}")
            }

            if (gameMode == GameMode.RECURSIVE) {
                if (deckHistory[0].any { it == decks[0] } || deckHistory[1].any { it == decks[1] }) {
                    if (debug) println("Infinite loop detected. Player 1 wins game $game")
                    decks[1].clear()
                    return
                } else {
                    deckHistory[0].add(decks[0].toList())
                    deckHistory[1].add(decks[1].toList())
                }
            }

            val cards = decks.map { it.removeFirst() }

            if (debug) {
                println("Player 1 plays: ${cards[0]}")
                println("Player 2 plays: ${cards[1]}")
            }

            val winner = determineRoundWinner(cards)
            if (debug) println("Player ${winner + 1} wins the round!\n")
        }

        fun determineRoundWinner(cards: List<Int>): Int {
            return when {
                gameMode == GameMode.RECURSIVE && decks[0].size >= cards[0] && decks[1].size >= cards[1] -> {
                    val subGame = Game(decks[0].take(cards[0]), decks[1].take(cards[1]), gameMode, game + 1)
                    val winner = subGame.playUntilWinner()
                    if (winner == 0) {
                        decks[0].addAll(cards)
                    } else {
                        decks[1].addAll(cards.reversed())
                    }
                    return winner
                }
                cards[0] > cards[1] -> {
                    decks[0].addAll(cards)
                    0
                }
                cards[1] > cards[0] -> {
                    decks[1].addAll(cards.reversed())
                    1
                }
                else ->
                    throw IllegalStateException()
            }
        }

        fun hasWinner(): Boolean = decks.any { it.isEmpty() }

        fun calculateWinningScore(): Int {
            val winningDeck = if (decks[0].isNotEmpty()) 0 else 1
            return decks[winningDeck].mapIndexed { i, v -> (decks[winningDeck].size - i) * v }.sum()
        }
    }

    override fun part1(): Int {
        val (input1, input2) = getInput()
            .split("\n\n")
            .map { deck -> deck.split("\n").drop(1).map(String::toInt) }

        val game = Game(input1, input2)
        game.playUntilWinner()

        return game.calculateWinningScore()
    }

    override fun part2(): Int {
        val (input1, input2) = getInput()
            .split("\n\n")
            .map { deck -> deck.split("\n").drop(1).map(String::toInt) }

        val game = Game(input1, input2, GameMode.RECURSIVE)
        game.playUntilWinner()

        return game.calculateWinningScore()
    }
}