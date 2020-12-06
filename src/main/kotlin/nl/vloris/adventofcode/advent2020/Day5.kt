package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

class Day5 : BaseSolver(2020, 5) {
    override fun part1() {
        val seatIds = convertToSeatIds(getInputLines())

        println(seatIds.maxOrNull())
    }

    override fun part2() {
        val seatIds = convertToSeatIds(getInputLines()).sorted()
        println(seatIds)

        for (i in 1..seatIds.size - 2) {
            if (seatIds[i] - seatIds[i-1] == 2) {
                println(seatIds[i]-1)
                return
            }
        }
    }

    private fun convertToSeatIds(input: List<String>): List<Int> {
        return input.map { s ->
            val binary = s
                .replace('F', '0')
                .replace('B', '1')
                .replace('L', '0')
                .replace('R', '1')

            Integer.parseInt(binary, 2)
        }
    }
}