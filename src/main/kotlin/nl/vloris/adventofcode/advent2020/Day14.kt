fun main(args: Array<String>) {
	println("Part 1: ${Day14.part1()}")
	println("Part 2: ${Day14.part2()}")
}

private class Mask(mask: String) {
	val offMask: Long
	val onMask: Long
	
	init {
		offMask = mask.replace('X', '0').toLong(2)
		onMask = mask.replace('X', '1').toLong(2)
	}
	
	fun apply(input: Long): Long = input and onMask or offMask
	
	fun apply2(input: Long): List<Long> {
		val diff = offMask xor onMask
		val result = mutableListOf<Long>()
		for (i in 0 until (1 shl diff.countOneBits())) {
			var x = input or offMask
			var k = diff
			for (j in 0 until diff.countOneBits()) {
				x = k xor k - (i shr j and 1) and diff xor x
				k = k and k - 1
			}
			result.add(x)
		}
        return result
	}
}

object Day14{
	private val maskRegex = Regex("""mask = (\w+)""")
	private val writeRegex = Regex("""mem\[(\d+)\] = (\d+)""")

	fun part1(): Long {
		val mem = mutableMapOf<Int, Long>()
		var mask = Mask("0")
	
		for (line in getInputLines()) {
			when {
				line.startsWith("mask") -> {
					maskRegex.matchEntire(line)?.let {
						mask = Mask(it.groupValues[1])
					}
				}
				line.startsWith("mem") -> {
					writeRegex.matchEntire(line)?.let {
						val address = it.groupValues[1].toInt()
						val value = mask.apply(it.groupValues[2].toLong())

                        mem[address] = mask.apply(value)
					}		
				}
				else -> throw IllegalArgumentException()
			}
		}
		
		return mem.values.sum()
	}
	
	fun part2(): Long {
		val mem = mutableMapOf<Long, Long>()
		var mask = Mask("0")
		
		for (line in getInputLines()) {
			when {
				line.startsWith("mask") -> {
					maskRegex.matchEntire(line)?.let {
						mask = Mask(it.groupValues[1])
					}
				}
				line.startsWith("mem") -> {
					writeRegex.matchEntire(line)?.let {
						val addresses = mask.apply2(it.groupValues[1].toLong())
						val value = it.groupValues[2].toLong()
				
						addresses.forEach {
							mem[it] = value
						}		
					}		
				}
				else -> throw IllegalArgumentException()
			}
		}
	
		return mem.values.sum()
	}
	
	private fun getDummyInputLines() = """mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
mem[8] = 11
mem[7] = 101
mem[8] = 0""".lines()
	
	private fun getInputLines() = generateSequence(::readLine).toList()
}