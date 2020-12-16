package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day16.solve()

object Day16 : BaseSolver(2020, 16) {
    override fun part1(): Int {
        val (rules, _, otherTickets) = parseInput()

        val invalidNumbers = otherTickets.map { it.fields }.toList().flatten().filter { field ->
            rules.all { rule -> !rule.isValid(field) }
        }
        return invalidNumbers.sum()
    }

    override fun part2(): Long {
        val (rules, myTicket, otherTickets) = parseInput()

        val validTickets = otherTickets.filter { ticket -> ticket.isValid(rules) }
        val totalFields = validTickets.first().fields.size

        val fieldRules = mutableMapOf<Rule, Int>()
        val remainingRules = rules.toMutableList()
        val remainingFields = (0 until totalFields).toMutableSet()

        while (remainingRules.isNotEmpty()) {
            for (rule in remainingRules) {
                val validFields = remainingFields.toMutableSet()
                for (ticket in validTickets) {
                    ticket.fields.forEachIndexed { i, field ->
                        if (!rule.isValid(field)) validFields.remove(i)
                    }
                }

                if (validFields.size == 1) {
                    fieldRules[rule] = validFields.first()
                    remainingFields.remove(validFields.first())
                }
            }

            remainingRules.removeAll { fieldRules.keys.contains(it) }
        }

        return fieldRules
            .filter { entry -> entry.key.name.startsWith("departure ") }
            .map { entry -> myTicket.fields[entry.value].toLong() }
            .reduce { acc, i -> acc * i }
    }

    private fun parseInput(): Triple<List<Rule>, Ticket, List<Ticket>> {
        val (ruleInput, ticketInput, otherInput) = getInput().split("\n\n")

        val rules = ruleInput.lines().map { it.asRule() }
        val ticket = ticketInput.lines().last().asTicket()
        val tickets = otherInput.lines().drop(1).map { it.asTicket() }

        return Triple(rules, ticket, tickets)
    }

    private val RULE_REGEX = """([^:]+): (\d+)-(\d+) or (\d+)-(\d+)""".toRegex()

    private fun String.asRule(): Rule {
        val (name, start1, end1, start2, end2) = RULE_REGEX.matchEntire(this)!!.destructured
        return Rule(name, listOf(IntRange(start1.toInt(), end1.toInt()), IntRange(start2.toInt(), end2.toInt())))
    }

    private fun String.asTicket(): Ticket = this.split(',').map(String::toInt).let { Ticket(it) }

    private data class Rule(val name: String, val ranges: List<IntRange>) {
        fun isValid(number: Int) = ranges.any { it.contains(number) }
        override fun toString(): String {
            return name
        }
    }

    private data class Ticket(val fields: List<Int>) {
        fun isValid(rules: List<Rule>) = fields.all { field -> rules.any { rule -> rule.isValid(field) } }
    }

    private fun getDummyInput() = """class: 0-1 or 4-19
row: 0-5 or 8-19
seat: 0-13 or 16-19

your ticket:
11,12,13

nearby tickets:
3,9,18
15,1,5
5,14,9"""
}