package nl.vloris.adventofcode.common

class Graph<V> {
    private val adjacentVertices = mutableMapOf<V, MutableList<V>>()

    constructor(edges: List<Pair<V, V>>) {
        edges.forEach { (from, to) -> addEdge(from, to) }
    }

    fun addEdge(from: V, to: V) {
        val list = adjacentVertices.getOrDefault(from, mutableListOf())
        list.add(to)
        adjacentVertices[from] = list
    }

    fun isReachable(destination: V, source: V): Boolean =
        depthFirstTraversal(source).contains(destination)

    fun depthFirstTraversal(root: V): Set<V> {
        val visited = mutableSetOf<V>()
        val stack = mutableListOf<V>()

        stack.add(root)
        while (stack.isNotEmpty()) {
            val vertex = stack.removeLast()
            if (!visited.contains(vertex)) {
                visited.add(vertex)
                adjacentVertices[vertex]?.forEach {
                    stack.add(it)
                }
            }
        }

        return visited
    }
}