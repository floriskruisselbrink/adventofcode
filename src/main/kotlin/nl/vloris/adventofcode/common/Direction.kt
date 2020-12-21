package nl.vloris.adventofcode.common

enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    companion object {
        fun from(char: Char): Direction = when (char.toUpperCase()) {
            'N' -> NORTH
            'E' -> EAST
            'S' -> SOUTH
            'W' -> WEST
            else -> throw IllegalArgumentException("Can't map $char to Direction")
        }
    }
}