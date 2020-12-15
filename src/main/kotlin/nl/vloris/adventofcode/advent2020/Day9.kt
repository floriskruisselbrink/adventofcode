package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day9.solve()

object Day9 : BaseSolver(2020, 9) {
    override fun part1(): Long {
        val input = getInputLines().map(String::toLong)
        return findInvalidNumber(input, 25)
    }

    override fun part2(): Long? {
        val input = getInputLines().map(String::toLong)
        val invalidNumber = findInvalidNumber(input, 25)

        for (fromIndex in 0 until input.size) {
            var sum = input[fromIndex]
            var toIndex = fromIndex

            while (sum < invalidNumber) {
                toIndex++
                sum += input[toIndex]
            }

            if (sum == invalidNumber) {
                return calculateWeakness(fromIndex, toIndex, input)
            }
        }

        return null
    }

    private fun findInvalidNumber(list: List<Long>, preambleSize: Int): Long {
        for (i in preambleSize until list.size) {
            if (!isValidNumber(i, list, preambleSize)) {
                return list[i]
            }
        }

        throw IllegalStateException("Nothing found")
    }

    private fun isValidNumber(index: Int, list: List<Long>, preambleSize: Int): Boolean {
        // preamble is always valid
        if (index < preambleSize) return true

        val checklist = list.subList(index - preambleSize, index).toSet()
        for (x in checklist) {
            for (y in checklist) {
                if (x == y) break
                if (x + y == list[index]) return true
            }
        }

        return false
    }

    private fun calculateWeakness(fromIndex: Int, toIndex: Int, list: List<Long>): Long {
        val weakList = list.subList(fromIndex, toIndex)
        return weakList.minOrNull()!! + weakList.maxOrNull()!!
    }
}