
fun main(args: Array<String>) {
	println("Part 1: ${Day13.part1()}")
	println("Part 2: ${Day13.part2()}")
}


object Day13 {
	fun part1(): Int {
		val input = getInputLines()
	
		val earliestTimestamp = input[0].toInt()
		val schedules = input[1].split(',')
			.filter { it != "x" }
			.map(String::toInt)
			.sorted()
		
		var timestamp = earliestTimestamp
		while (true) {
			for (schedule in schedules) {
				if (timestamp.rem(schedule) == 0) {
					return schedule * (timestamp-earliestTimestamp)
				}
			}
			timestamp++
		}
	}
	
	fun part2(): Long {
		val input = getInputLines()
		val schedules = input[1].split(',')
			.mapIndexed { i, bus -> bus to i}
			.filterNot { it.first == "x" }
			.map { (bus, i) -> bus.toInt() to i }
	
		var timestamp = 0L
		var increment = 1L
		
		for ((id, i) in schedules) {
			while ((timestamp + i) % id != 0L) {
				timestamp += increment
			}
			increment *= id
		}
		
		return timestamp
	}
	
	private fun getDummyInput() = listOf("939", "7,13,x,x,59,x,31,19")
	
	private fun getInputLines() = generateSequence(::readLine).toList()
}