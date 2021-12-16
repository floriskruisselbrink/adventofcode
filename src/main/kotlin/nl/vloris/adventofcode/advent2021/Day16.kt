package nl.vloris.adventofcode.advent2021

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day16.solve()

object Day16 : BaseSolver(2021, 16) {
    override fun part1(): Int {
        val input = parseInput(getInput())
        val packet = Packet.read(input)
        return packet.versionSum()
    }

    override fun part2(): Long {
        val input = parseInput(getInput())
        val packet = Packet.read(input)
        return packet.calculateValue()
    }

    private fun parseInput(input: String): Sequence<Char> = input.asSequence().flatMap {
        it.digitToInt(16).toString(2).padStart(4, '0').toList()
    }

}

private enum class LengthTypeId(val bits: Int) { TOTAL_LENGTH_BITS(15), NUMBER_OF_SUBPACKETS(11) }

private fun Int.toLengthTypeId() = if (this == 0) LengthTypeId.TOTAL_LENGTH_BITS else LengthTypeId.NUMBER_OF_SUBPACKETS

private class BitStream(val stream: Iterator<Char>) {
    var counter = 0
        private set

    fun nextInt(bits: Int) = (0 until bits).map { counter++; stream.next() }.joinToString("").toInt(2)
}

private sealed class Packet {
    abstract val version: Int

    abstract fun versionSum(): Int
    abstract fun calculateValue(): Long

    data class LiteralPacket(override val version: Int, val value: Long) : Packet() {
        override fun versionSum(): Int = version
        override fun calculateValue(): Long = value
    }

    data class OperatorPacket(override val version: Int, val type: Int, val contents: List<Packet>) : Packet() {
        override fun versionSum(): Int = version + contents.sumOf { it.versionSum() }
        override fun calculateValue(): Long = when (type) {
            0 -> contents.sumOf { it.calculateValue() }
            1 -> contents.map { it.calculateValue() }.reduce(Long::times)
            2 -> contents.minOf { it.calculateValue() }
            3 -> contents.maxOf { it.calculateValue() }
            5 -> if (contents.first().calculateValue() < contents.last().calculateValue()) 1L else 0L
            6 -> if (contents.first().calculateValue() > contents.last().calculateValue()) 1L else 0L
            7 -> if (contents.first().calculateValue() == contents.last().calculateValue()) 1L else 0L
            else -> throw IllegalArgumentException("Unknown operator type id $type")
        }
    }

    companion object {
        fun read(input: Sequence<Char>) = read(BitStream(input.iterator()))
        fun read(stream: BitStream): Packet {
            val version = stream.nextInt(3)
            val typeId = stream.nextInt(3)

            return when (typeId) {
                4 -> readLiteralValue(stream, version)
                else -> readOperatorValue(stream, typeId, version)
            }
        }

        private fun readLiteralValue(stream: BitStream, version: Int): LiteralPacket {
            var result = 0L
            do {
                val hasNext = stream.nextInt(1) == 1
                result = (result shl 4) + stream.nextInt(4)
            } while (hasNext)

            return LiteralPacket(version, result)
        }

        private fun readOperatorValue(stream: BitStream, typeId: Int, version: Int): OperatorPacket {
            val lengthTypeId = stream.nextInt(1).toLengthTypeId()
            val length = stream.nextInt(lengthTypeId.bits)

            val subPackets = when (lengthTypeId) {
                LengthTypeId.TOTAL_LENGTH_BITS -> readPacketsUntil(stream, length)
                LengthTypeId.NUMBER_OF_SUBPACKETS -> readPacketsCount(stream, length)
            }

            return OperatorPacket(version, typeId, subPackets)
        }

        private fun readPacketsCount(stream: BitStream, count: Int): List<Packet> {
            return (0 until count).map { read(stream) }
        }

        private fun readPacketsUntil(stream: BitStream, bitsToRead: Int): List<Packet> {
            val startCounter = stream.counter
            val subPackets = mutableListOf<Packet>()
            while (stream.counter < startCounter + bitsToRead) {
                subPackets.add(read(stream))
            }

            return subPackets
        }
    }
}
