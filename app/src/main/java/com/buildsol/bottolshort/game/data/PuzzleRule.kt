package com.buildsol.bottolshort.game.data

typealias ColorId = Int
typealias BottleContents = List<ColorId>
typealias PuzzleState = List<BottleContents>

data class Move(val from: Int, val to: Int)

object PuzzleRules {
    fun isSolved(state: PuzzleState, capacity: Int): Boolean =
        state.all { it.isEmpty() || (it.size == capacity && it.all { c -> c == it[0] }) }

    private fun topRun(bottle: BottleContents): Int {
        if (bottle.isEmpty()) return 0
        val top = bottle.last()
        var n = 0
        for (i in bottle.indices.reversed()) { if (bottle[i] == top) n++ else break }
        return n
    }

    fun canPour(state: PuzzleState, from: Int, to: Int, capacity: Int): Boolean {
        if (from == to) return false
        val src = state[from]; val dst = state[to]
        if (src.isEmpty() || dst.size >= capacity) return false
        return dst.isEmpty() || dst.last() == src.last()
    }

    fun pour(state: PuzzleState, from: Int, to: Int, capacity: Int): PuzzleState {
        val src = state[from]; val dst = state[to]
        val amount = minOf(topRun(src), capacity - dst.size)
        val moving = src.subList(src.size - amount, src.size).toList()
        return state.toMutableList().apply {
            this[from] = src.subList(0, src.size - amount).toList()
            this[to] = dst + moving
        }
    }

    fun legalMoves(state: PuzzleState, capacity: Int): List<Move> = buildList {
        for (i in state.indices) {
            if (state[i].isEmpty()) continue
            for (j in state.indices) if (i != j && canPour(state, i, j, capacity)) add(Move(i, j))
        }
    }
}