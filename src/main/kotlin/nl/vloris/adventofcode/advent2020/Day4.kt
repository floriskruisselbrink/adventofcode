package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

class Day4 : BaseSolver(2020, 4) {
    private val requiredFields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

    override fun part1() {
        val passportData = getInput()
            .split("\n\n")
            .map { p -> p.split(' ', '\n') }

        val validPassports = passportData
            .map { p -> p.map { f -> f.split(':').first() } }
            .filter { p -> p.containsAll(requiredFields) }

        println(validPassports.size)
    }

    override fun part2() {
        val passportData = getInput()
            .split("\n\n")
            .map { p -> p.split(' ', '\n') }

        val validPassports = passportData
            .map { fields ->
                Passport(fields
                    .filter(String::isNotEmpty)
                    .map { Pair(it.substringBefore(':'), it.substringAfter(':')) }
                    .toMap())
            }
            .filter(Passport::isValid)

        println(validPassports.size)
    }

    data class Passport(val fields: Map<String, String>) {
        fun isValid(): Boolean {
            return validateNumber(fields["byr"], 1920, 2002) &&
                    validateNumber(fields["iyr"], 2010, 2020) &&
                    validateNumber(fields["eyr"], 2020, 2030) &&
                    validateHeight(fields["hgt"]) &&
                    validateRegex(fields["hcl"], Regex("^#[a-f0-9]{6}$")) &&
                    validateRegex(fields["ecl"], Regex("^(amb|blu|brn|gry|grn|hzl|oth)$")) &&
                    validateRegex(fields["pid"], Regex("^[0-9]{9}$"))
        }

        private fun validateNumber(value: String?, minValue: Int, maxValue: Int): Boolean {
            val number = value?.toIntOrNull()
            return number in minValue..maxValue
        }

        private fun validateHeight(value: String?): Boolean {
            return when {
                value == null -> {
                    false
                }
                value.endsWith("in") -> {
                    validateNumber(value.removeSuffix("in"), 59, 76)
                }
                value.endsWith("cm") -> {
                    validateNumber(value.removeSuffix("cm"), 150, 193)
                }
                else -> {
                    false
                }
            }
        }

        private fun validateRegex(value: String?, regex: Regex): Boolean {
            return value?.matches(regex) ?: false
        }
    }
}