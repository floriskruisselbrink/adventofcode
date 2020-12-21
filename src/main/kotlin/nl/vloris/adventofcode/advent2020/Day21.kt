package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day21.solve()

typealias Allergen = String
typealias Ingredient = String

object Day21 : BaseSolver(2020, 21) {
    private data class Food(val ingredients: Set<Ingredient>, val allergens: Set<Allergen>) {
        constructor(input: String) : this(
            input.substringBefore(" (contains ").split(' ').toSet(),
            input.dropLast(1).substringAfter(" (contains ").split(", ").toSet()
        )
    }

    private fun findKnownAllergens(input: List<Food>): Map<Allergen, Ingredient> {
        val allAllergens = input.map { it.allergens }.flatten().toSet()
        val knownAllergens = mutableMapOf<Allergen, Ingredient>()

        while (knownAllergens.size < allAllergens.size) {
            for (allergen in allAllergens) {
                input
                    // 1. find all foods with this allergen
                    .filter { it.allergens.contains(allergen) }
                    // 2. find the ingredients that are in all of those foods
                    .map { it.ingredients }
                    .reduce { acc, list -> acc intersect list }
                    // 3. remove already known ingredients from this list
                    .minus(knownAllergens.values)
                    // 4. if this is only one ingredient, it *must* be the allergen
                    .firstOrNull()
                    ?.let { knownAllergens[allergen] = it }
            }
        }

        return knownAllergens
    }

    override fun part1(): Int {
        val input = getInputLines().map { line -> Food(line) }
        val knownAllergens = findKnownAllergens(input)

        return input
            .map { it.ingredients }
            .flatten()
            .filterNot { it in knownAllergens.values }
            .count()
    }

    override fun part2(): String {
        val input = getInputLines().map { line -> Food(line) }
        return findKnownAllergens(input)
            .toSortedMap()
            .values
            .joinToString(",")
    }

    private fun getExampleInputLines(): List<String> = """sqjhc fvjkl (contains soy)
mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
trh fvjkl sbzzf mxmxvkd (contains dairy)
sqjhc mxmxvkd sbzzf (contains fish)""".lines()
}