import kotlin.math.abs

fun main(args: Array<String>) {
	println("Part 1: ${Day12.part1()}")
	println("Part 2: ${Day12.part2()}")
}


private val DIRECTIONS = listOf('E', 'S', 'W', 'N')

private class Ship {
	var x = 0
	var y = 0
	var facing = 0
	
	fun move(direction: Char, length: Int) {
		when (direction) {
			'F' -> move(DIRECTIONS[facing], length)
			'L' -> facing = (facing + 4 - (length/90)).rem(DIRECTIONS.size)
			'R' -> facing = (facing + (length/90)).rem(DIRECTIONS.size)
			'N' -> y -= length
			'E' -> x += length
			'S' -> y += length
			'W' -> x -= length
			else -> throw IllegalArgumentException()
		}
	}
}

private data class Point(val x: Int, val y: Int) {
	fun rotateRight() = Point(-y, x)
	
	fun rotateRight(count: Int): Point {
		var result = this
		repeat(count) { result = result.rotateRight() }
		return result
	}
	
	fun rotateLeft() = Point(y, -x)
	
	fun rotateLeft(count: Int): Point {
		var result = this
		repeat(count) { result = result.rotateLeft() }
		return result
	}
}

object Day12 {
	fun part1(): Int {
		val ship = Ship()
		for (line in getInputLines()) {
			val direction = line[0]
			val length = line.drop(1).toInt()
			
			ship.move(direction, length)
		}
	
		return abs(ship.x) + abs(ship.y)
	}
	
	fun part2(): Int {
		var ship = Point(0, 0)
		var waypoint = Point(10, -1)
		
		for (line in getInputLines()) {
			val direction = line[0]
			val length = line.drop(1).toInt()
			
			when (direction) {
				'N' -> waypoint = Point(waypoint.x, waypoint.y-length)
				'E' -> waypoint = Point(waypoint.x+length, waypoint.y)
				'S' -> waypoint = Point(waypoint.x, waypoint.y+length)
				'W' -> waypoint = Point(waypoint.x-length, waypoint.y)
				'L' -> waypoint = waypoint.rotateLeft(length/90)
				'R' -> waypoint = waypoint.rotateRight(length/90)
				'F' -> ship = Point(ship.x + waypoint.x * length, ship.y + waypoint.y * length)
				else -> throw IllegalArgumentException()
			}
		}
		
		return abs(ship.x) + abs(ship.y)
	}

	
	fun getDummyInputLines(): List<String> =
	"""F10
N3
F7
L270
F11""".lines()

	fun getInputLines() = generateSequence(::readLine)	
}