package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver
import nl.vloris.adventofcode.common.Graph

class Day8 : BaseSolver(2020, 8) {
    sealed class Operation {
        data class Nop(val argument: Int) : Operation()
        data class Acc(val argument: Int) : Operation()
        data class Jmp(val argument: Int) : Operation()

        fun nextIp(ip: Int): Int =
            when (this) {
                is Jmp -> ip + argument
                else -> ip + 1
            }

        fun fixme(): Operation =
            when (this) {
                is Nop -> Jmp(argument)
                is Jmp -> Nop(argument)
                else -> this
            }
    }

    override fun part1() {
        val operations = readInput()
        val seenAddresses = mutableSetOf<Int>()
        val result = run(operations) { ip -> !seenAddresses.add(ip) }

        println(result)
    }

    override fun part2() {
        val originalOperations = readInput()

        var iteration = 0
        var operations = modifyOperations(originalOperations, iteration)

        while (iteration < operations.size && !doesProgramTerminate(operations)) {
            iteration++
            operations = modifyOperations(originalOperations, iteration)
        }

        val acc = run(operations) { ip -> ip == operations.size }
        println(acc)
    }

    private fun run(operations: List<Operation>, exitCondition: (Int) -> Boolean): Int {
        var ip = 0
        var acc = 0

        while (!exitCondition(ip)) {
            val op = operations[ip]
            if (op is Operation.Acc) acc += op.argument
            ip = op.nextIp(ip)
        }

        return acc
    }

    private fun modifyOperations(operations: List<Operation>, iteration: Int): List<Operation> =
        operations
            .withIndex()
            .map { (index, operation) ->
                if (index == iteration) operation.fixme()
                else operation
            }

    private fun doesProgramTerminate(operations: List<Operation>): Boolean =
        createGraph(operations).isReachable(0, operations.size)

    private fun createGraph(operations: List<Operation>): Graph<Int> =
        Graph(
            operations.withIndex()
                .map { (index, operation) ->
                    operation.nextIp(index) to index
                }
        )

    private fun readInput(): List<Operation> {
        val regex = """(\w+) ([-+]\d+)""".toRegex()

        return getInputLines()
            .map { line ->
                val (operation, parameter) = regex.find(line)!!.destructured
                when (operation) {
                    "nop" -> Operation.Nop(parameter.toInt())
                    "acc" -> Operation.Acc(parameter.toInt())
                    "jmp" -> Operation.Jmp(parameter.toInt())
                    else -> throw IllegalArgumentException("Operation $operation does not exist")
                }
            }
    }

    private fun getDummyInputLines(): List<String> {
        return """nop +0
acc +1
jmp +4
acc +3
jmp -3
acc -99
acc +1
jmp -4
acc +6""".lines()
    }
}

