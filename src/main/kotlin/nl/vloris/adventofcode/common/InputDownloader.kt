package nl.vloris.adventofcode.common

import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class InputDownloader(
    private val year: Int,
    private val day: Int
) {
    private val sessionId = checkNotNull(System.getenv("AOC_SESSION")) { "Environment parameter AOC_SESSION must be set"}

    private val file = File("out/input-$year-$day.txt")

    fun getInput(): String {
        if (!fileExists()) {
            download()
        }

        return file.readText()
    }

    private fun fileExists(): Boolean {
        return file.exists() && file.canRead()
    }

    private fun download() {
        val url = URL("https://adventofcode.com/$year/day/$day/input")

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"
            setRequestProperty("Cookie", "session=$sessionId;")

            println("Downloading $url...")

            inputStream.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
    }
}