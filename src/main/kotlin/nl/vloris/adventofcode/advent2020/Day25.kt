package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day25.solve()

object Day25 : BaseSolver(2020, 25) {
    private const val MODULUS = 20201227

    override fun part1(): Long {
        val (cardPublicKey, doorPublicKey) = getInputLines().map(String::toInt)
        println("Public keys: card = $cardPublicKey, door = $doorPublicKey")

        val cardLoop = findLoopSize(cardPublicKey)
        val doorLoop = findLoopSize(doorPublicKey)
        println("Loop size: card = $cardLoop, door = $doorLoop")

        val cardEncryption = findEncryptionKey(cardLoop, doorPublicKey)
        val doorEncryption = findEncryptionKey(doorLoop, cardPublicKey)
        println("Encryption key: $cardEncryption / $doorEncryption")

        return cardEncryption
    }

    override fun part2() = 0

    private fun findLoopSize(publicKey: Int): Int {
        val subjectNumber = 7
        var value = 1
        var loop = 0

        while (value != publicKey) {
            loop++
            value *= subjectNumber
            value %= MODULUS
        }

        return loop
    }

    private fun findEncryptionKey(loopSize: Int, subjectNumber: Int): Long {
        var value = 1L
        for (loop in 1..loopSize) {
            value *= subjectNumber
            value %= MODULUS
        }

        return value
    }
}