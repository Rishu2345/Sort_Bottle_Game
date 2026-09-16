package com.buildsol.bottolshort.game.data

import java.util.PriorityQueue

object PuzzleSolver {
    sealed class Result {
        data class Solved(val moveCount: Int) : Result()
        object NotFound : Result()
    }

    private fun heuristic(state: PuzzleState): Int {
        var h = 0
        for (bottle in state) {
            if (bottle.isEmpty()) continue
            var groups = 1
            for (i in 1 until bottle.size) if (bottle[i] != bottle[i - 1]) groups++
            h += groups - 1
        }
        return h
    }

    fun solve(start: PuzzleState, capacity: Int, nodeBudget: Int): Result {
        if (PuzzleRules.isSolved(start, capacity)) return Result.Solved(0)

        data class Node(val state: PuzzleState, val g: Int, val h: Int) { val f get() = g + h }

        val frontier = PriorityQueue<Node>(compareBy { it.f })
        val bestG = HashMap<PuzzleState, Int>()
        frontier += Node(start, 0, heuristic(start)); bestG[start] = 0

        var expansions = 0
        while (frontier.isNotEmpty() && expansions < nodeBudget) {
            val node = frontier.poll(); expansions++
            if (PuzzleRules.isSolved(node.state, capacity)) return Result.Solved(node.g)
            if ((bestG[node.state] ?: Int.MAX_VALUE) < node.g) continue

            for (move in PuzzleRules.legalMoves(node.state, capacity)) {
                val src = node.state[move.from]
                if (src.size == capacity && src.all { it == src[0] }) continue
                val next = PuzzleRules.pour(node.state, move.from, move.to, capacity)
                val g2 = node.g + 1
                if (g2 < (bestG[next] ?: Int.MAX_VALUE)) {
                    bestG[next] = g2
                    frontier += Node(next, g2, heuristic(next))
                }
            }
        }
        return Result.NotFound
    }
}