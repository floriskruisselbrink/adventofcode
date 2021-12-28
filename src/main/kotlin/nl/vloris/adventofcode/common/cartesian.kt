package nl.vloris.adventofcode.common

fun <T> Iterable<T>.cartesian(): Sequence<Pair<T, T>> =
    this.asSequence().flatMap { a ->
        this.asSequence().map { b ->
            Pair(a, b)
        }
    }