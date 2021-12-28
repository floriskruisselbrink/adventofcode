package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver
import nl.vloris.adventofcode.common.cartesian

fun main() = Day18.solve()

object Day18 : BaseSolver(2021, 18) {
    override fun part1(): Long = getInputLines()
        .map { parse(it) }
        .reduce(SnailNumber::plus)
        .magnitude()

    override fun part2(): Long? = getInputLines()
        .cartesian()
        .filterNot { it.first == it.second }
        .map { (parse(it.first) + parse(it.second)).magnitude() }
        .maxOrNull()

    private fun parse(line: String): SnailNumber {
        val values = mutableListOf<SnailNumber>()

        line.forEach { character ->
            when (character) {
                in '0'..'9' -> values.add(NumberValue(character.digitToInt().toLong()))
                ']' -> {
                    val right = values.removeLast()
                    val left = values.removeLast()
                    values.add(NumberPair(left, right))
                }
            }
        }

        return values.first()
    }
}

private sealed class SnailNumber {
    var parent: NumberPair? = null

    operator fun plus(other: SnailNumber): SnailNumber = NumberPair(this, other).reduce()

    fun reduce(): SnailNumber {
        // try exploding every pair nested below level 4
        val toExplode =
            traverse(0).filter { it.second == 4 }.map { it.first }.filterIsInstance<NumberPair>().firstOrNull()
        if (toExplode != null) {
            val left =
                traverse(0).map { it.first }
                    .takeWhile { it != toExplode }
                    .filterIsInstance<NumberValue>()
                    .lastOrNull()
            val right =
                traverse(0).map { it.first }
                    .dropWhile { it != toExplode }
                    .filterIsInstance<NumberValue>()
                    .drop(2)
                    .firstOrNull()

            toExplode.explode(left, right)
            return reduce()
        }

        // then split all values greater than 9
        val toSplit = traverse(0).map { it.first }.filterIsInstance<NumberValue>().firstOrNull { it.value > 9 }
        if (toSplit != null) {
            toSplit.split()
            return reduce()
        }

        return this
    }

    abstract fun traverse(depth: Int): Sequence<Pair<SnailNumber, Int>>

    abstract fun magnitude(): Long
}

private class NumberPair(var left: SnailNumber, var right: SnailNumber) : SnailNumber() {
    init {
        left.parent = this
        right.parent = this
    }

    override fun magnitude(): Long = 3 * left.magnitude() + 2 * right.magnitude()

    fun explode(leftTarget: NumberValue?, rightTarget: NumberValue?) {
        val newValue = NumberValue(0)
        newValue.parent = this.parent

        if (parent!!.left == this) parent!!.left = newValue else parent!!.right = newValue

        leftTarget?.let { it.value += (left as NumberValue).value }
        rightTarget?.let { it.value += (right as NumberValue).value }
    }

    override fun traverse(depth: Int): Sequence<Pair<SnailNumber, Int>> =
        sequenceOf(this to depth) + left.traverse(depth + 1) + right.traverse(depth + 1)

    override fun toString(): String = "[$left,$right]"
}

private class NumberValue(var value: Long) : SnailNumber() {
    override fun magnitude(): Long = value

    override fun traverse(depth: Int): Sequence<Pair<SnailNumber, Int>> {
        return sequenceOf(this to depth)
    }

    fun split() {
        val newPair = NumberPair(NumberValue(value / 2), NumberValue((value + 1) / 2))
        newPair.parent = this.parent

        if (parent!!.left == this) parent!!.left = newPair else parent!!.right = newPair
    }

    override fun toString(): String = value.toString()
}