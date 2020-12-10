package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day7.solve()


object Day7 : BaseSolver(2020, 7) {
    data class Bag(val color: String)
    data class Rule(val bag: Bag, val innerBags: List<Pair<Int, Bag>>) {
        fun contains(innerBag: Bag): Boolean =
            innerBags.any { (_, bag) -> bag == innerBag }
    }

    override fun part1(): Int {
        val rules = readInput()
        val outerBags = findOuterBag(Bag("shiny gold"), rules)
        return outerBags.distinct().count()
    }

    private fun findOuterBag(innerBag: Bag, rules: List<Rule>): List<Bag> {
        val outerBags = rules
            .filter { it.contains(innerBag) }
            .map(Rule::bag)

        val moreOuterBags = outerBags.map { findOuterBag(it, rules) }.flatten()
        return outerBags + moreOuterBags
    }

    override fun part2(): Int {
        val rules = readInput()
        val innerBags = findInnerBags(Bag("shiny gold"), rules)

        return innerBags.sumBy { it.first }
    }

    private fun findInnerBags(outerBag: Bag, rules: List<Rule>): List<Pair<Int, Bag>> {
        val innerBags = rules
            .filter { it.bag == outerBag }
            .map(Rule::innerBags)
            .flatten()

        val innerInnerBags = innerBags
            .map { (count, bag) ->
                findInnerBags(bag, rules).map { (innerCount, innerBag) ->
                    Pair(count * innerCount, innerBag)
                }
            }.flatten()

        return innerBags + innerInnerBags
    }

    private fun readInput(): List<Rule> {
        return getInputLines()
            .map { createRule(it.removeSuffix(".")) }
    }

    private fun createRule(input: String): Rule {
        val (outer, inner) = input.split(" bags contain ")

        val outerBag = Bag(outer)

        if (inner == "no other bags") {
            return Rule(outerBag, emptyList())
        }

        val innerBags = inner.split(", ").map { bags ->
            val (count, bag) = bags.split(" ", limit = 2)
            val color = bag.removeSuffix(" bag").removeSuffix(" bags")
            Pair(count.toInt(), Bag(color))
        }

        return Rule(outerBag, innerBags)
    }

    private fun getDummyInputLines(): List<String> {
        return """light red bags contain 1 bright white bag, 2 muted yellow bags.
dark orange bags contain 3 bright white bags, 4 muted yellow bags.
bright white bags contain 1 shiny gold bag.
muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
dark olive bags contain 3 faded blue bags, 4 dotted black bags.
vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
faded blue bags contain no other bags.
dotted black bags contain no other bags.""".lines()
    }
}