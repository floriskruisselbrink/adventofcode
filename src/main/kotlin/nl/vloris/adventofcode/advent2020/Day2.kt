package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day2.solve()

object Day2 : BaseSolver(2020, 2) {
    override fun part1(): Int {
        var validPasswords = 0

        for (inputLine in getInputLines()) {
            val (policy, password) = inputLine.split(": ")

            if (Policy1(policy).validate(password)) {
                validPasswords++
            }
        }

        return validPasswords
    }

    override fun part2(): Int {
        var validPasswords = 0

        for (inputLine in getInputLines()) {
            val (policy, password) = inputLine.split(": ")

            if (Policy2(policy).validate(password)) {
                validPasswords++
            }
        }

        return validPasswords
    }
}

data class Policy1(val policy: String) {
    private val min: Int
    private val max: Int
    private val letter: Char

    init {
        val split = policy.split('-', ' ')
        min = split[0].toInt()
        max = split[1].toInt()
        letter = split[2][0]
    }

    fun validate(password: String): Boolean {
        val count = password.filter { it == letter }.count()
        return count in min..max
    }
}

data class Policy2(val policy: String) {
    private val position1: Int
    private val position2: Int
    private val letter: Char

    init {
        val split = policy.split('-', ' ')
        position1 = split[0].toInt() - 1
        position2 = split[1].toInt() - 1
        letter = split[2][0]
    }

    fun validate(password: String): Boolean {
        return (password[position1] == letter)
            .xor(password[position2] == letter)
    }
}

