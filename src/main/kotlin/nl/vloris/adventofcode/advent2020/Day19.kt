package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day19.solve()

object Day19 : BaseSolver(2020, 19) {
    private sealed class Rule {
        data class Terminal(val char: Char) : Rule()
        data class Alternatives(val alternatives: List<List<Int>>) : Rule()

        companion object {
            fun parse(input: String): Rule = if (input.startsWith('"'))
                Rule.Terminal(input[1])
            else
                input.split(" | ")
                        .map { it.split(' ').map(String::toInt) }
                        .let(Rule::Alternatives)
        }
    }

    override fun part1(): Int {
        val (rulesInput, messages) = getInput().split("\n\n")
        val rules = rulesInput.toRules()

        return messages.lines().count { matches(it, rules) }
    }

    override fun part2(): Int {
        val (rulesInput, messages) = getInput().split("\n\n")

        val rules = rulesInput.toRules().toMutableList()
        rules[8] = Rule.parse("42 | 42 8")
        rules[11] = Rule.parse("42 31 | 42 11 31")

        return messages.lines().count { matches(it, rules) }
    }

    private fun matches(message: String, rules: List<Rule>): Boolean {
        fun match(unmatchedMessage: String, unmatchedRules: List<Int>): Boolean =
                when (val rule = unmatchedRules.firstOrNull()?.let(rules::get)) {
                    null -> unmatchedMessage.isEmpty()
                    is Rule.Terminal -> rule.char == unmatchedMessage.firstOrNull() &&
                            match(unmatchedMessage.drop(1), unmatchedRules.drop(1))
                    is Rule.Alternatives -> rule.alternatives.any { alt -> match(unmatchedMessage, alt + unmatchedRules.drop(1)) }
                }
        return match(message, listOf(0))
    }

    private fun String.toRules() = this.lines()
            .map { it.split(": ") }
            .sortedBy { it[0].toInt() }
            .map { Rule.parse(it[1]) }
}