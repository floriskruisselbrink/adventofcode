package nl.vloris.adventofcode.common

import java.io.File
import java.io.InputStream
import java.net.URL

private val AOC_SESSION =
    checkNotNull(System.getenv("AOC_SESSION")) { "Environment parameter AOC_SESSION must be set" }

fun retrieveInput(year: Int, day: Int) =
    readFromCache(year, day) ?: readFromUrl(year, day).also { write(it, year, day) }

private fun readFromCache(year: Int, day: Int) =
    File("data/$year/input-$day.txt").takeIf { it.exists() }?.readText()

private fun readFromUrl(year: Int, day: Int) =
    URL("https://adventofcode.com/$year/day/$day/input")
        .openConnection()
        .apply { addRequestProperty("Cookie", "session=$AOC_SESSION") }
        .getInputStream()
        .use(InputStream::readBytes)
        .toString(Charsets.UTF_8)
        .trimEnd()

private fun write(input: String, year: Int, day: Int) {
    File("data/$year").mkdirs()
    File("data/$year/input-$day.txt").writeText(input)
}
