package nl.vloris.adventofcode.advent2020

import nl.vloris.adventofcode.common.BaseSolver

fun main() = Day17.solve()

object Day17 : BaseSolver(2020, 17) {

    interface Point<P : Point<P>> {
        operator fun plus(other: P): P
        operator fun rangeTo(other: P): List<P>
        fun neighbours(): List<P>
        fun neighbours(predicate: (P) -> Boolean): List<P> = neighbours().filter(predicate)
    }

    private val neighbours3D = (Point3D(-1, -1, -1)..Point3D(1, 1, 1)).filter { it != Point3D(0, 0, 0) }

    data class Point3D(val x: Int, val y: Int, val z: Int) : Point<Point3D> {
        override operator fun plus(other: Point3D) = Point3D(x + other.x, y + other.y, z + other.z)
        override operator fun rangeTo(other: Point3D) = (x..other.x).map { x ->
            (y..other.y).map { y ->
                (z..other.z).map { z -> Point3D(x, y, z) }
            }
        }.flatten().flatten()

        override fun neighbours() = neighbours3D.map { this + it }

        override fun toString() = "P($x,$y,$z)"
    }

    private val neighbours4D = (Point4D(-1, -1, -1, -1)..Point4D(1, 1, 1, 1)).filter { it != Point4D(0, 0, 0, 0) }

    data class Point4D(val x: Int, val y: Int, val z: Int, val w: Int) : Point<Point4D> {
        override operator fun plus(other: Point4D) = Point4D(x + other.x, y + other.y, z + other.z, w + other.w)
        override operator fun rangeTo(other: Point4D) = (x..other.x).map { x ->
            (y..other.y).map { y ->
                (z..other.z).map { z ->
                    (w..other.w).map { w ->
                        Point4D(x, y, z, w)
                    }
                }
            }
        }.flatten().flatten().flatten()

        override fun neighbours() = neighbours4D.map { this + it }

        override fun toString() = "P($x,$y,$z,$w)"
    }

    object Rules {
        fun survives(liveNeighbours: Int) = liveNeighbours in 2..3
        fun born(liveNeighbours: Int) = liveNeighbours == 3
    }

    data class Universe<Cell : Point<Cell>>(private val life: Set<Cell>) {
        fun countAlive() = life.count()

        operator fun inc() = copy(life = survivingCells() + bornCells())

        private fun survivingCells() = life.filter { it.survives() }.toSet()
        private fun bornCells() = deadNeighboursOfLivingCells().filter { it.born() }.toSet()
        private fun deadNeighboursOfLivingCells() = life.flatMap { it.deadNeighbours() }

        private fun Cell.survives() = Rules.survives(countLiveNeighbours())
        private fun Cell.born() = Rules.born(countLiveNeighbours())
        private fun Cell.isAlive() = this in life
        private fun Cell.isDead() = !isAlive()
        private fun Cell.liveNeighbours() = neighbours { it.isAlive() }
        private fun Cell.deadNeighbours() = neighbours { it.isDead() }
        private fun Cell.countLiveNeighbours() = liveNeighbours().count()
    }

    override fun part1(): Int {
        val input = getInputLines().mapIndexed { y, str ->
            str.mapIndexed { x, char ->
                if (char == '#') Point3D(x, y, 0) else null
            }
        }.flatten().filterNotNull()

        var universe = Universe<Point3D>(input.toSet())
        repeat(6) { universe++ }

        return universe.countAlive()
    }

    override fun part2(): Int {
        val input = getInputLines().mapIndexed { y, str ->
            str.mapIndexed { x, char ->
                if (char == '#') Point4D(x, y, 0, 0) else null
            }
        }.flatten().filterNotNull()

        var universe = Universe<Point4D>(input.toSet())
        repeat(6) { universe++ }

        return universe.countAlive()
    }
}