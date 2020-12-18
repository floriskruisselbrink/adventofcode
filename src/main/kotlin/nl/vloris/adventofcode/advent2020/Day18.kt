package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver
import java.util.*

fun main() = Day18.solve()

object Day18 : BaseSolver(2020, 18) {
    override fun part1() = getInputLines().map { calculate(it, evaluatePart1) }.sum()
    override fun part2() = getInputLines().map { calculate(it, evaluatePart2) }.sum()

    private fun calculate(input: String, evalFunc: (Stack<String>) -> Unit): Long {
        val operationStack = Stack<String>()
        val parts = input.replace(" ", "").split("").filter { it.isNotBlank() }

        for (i in parts.size - 1 downTo 0) {
            when (parts[i]) {
                "(" -> evalFunc(operationStack)
                else -> operationStack.push(parts[i])
            }
        }

        evalFunc(operationStack)

        return operationStack.pop().toLong()
    }

    private val evaluatePart1: (Stack<String>) -> Unit = { operationStack ->
        var result = operationStack.pop().toLong()

        loop@ while (operationStack.isNotEmpty()) {
            when (operationStack.pop()) {
                "+" -> result += operationStack.pop().toLong()
                "*" -> result *= operationStack.pop().toLong()
                ")" -> break@loop
            }
        }

        operationStack.push(result.toString())
    }

    private val evaluatePart2: (Stack<String>) -> Unit = { operationStack ->
        val innerStack = Stack<String>()

        loop@ while (operationStack.isNotEmpty()) {
            when (val popped = operationStack.pop()) {
                "+" -> innerStack.push((operationStack.pop().toLong() + innerStack.pop().toLong()).toString())
                "*" -> innerStack.push(operationStack.pop())
                ")" -> break@loop
                else -> innerStack.push(popped)
            }
        }

        val result = innerStack.map { it.toLong() }.reduce { acc, value -> acc * value }

        operationStack.push(result.toString())
    }
}