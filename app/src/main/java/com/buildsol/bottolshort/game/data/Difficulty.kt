package com.buildsol.bottolshort.game.data

import androidx.compose.runtime.toMutableStateList
import com.buildsol.bottolshort.game.domain.model.Bottle
import com.buildsol.bottolshort.game.domain.model.Color

enum class Difficulty { EASY, MEDIUM, HARD, EXPERT }

data class LevelConfig(
    val numColors: Int,
    val bottleCapacity: Int = 4,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val seed: Long? = null,
    val maxAttempts: Int = 200,
    val solverNodeBudget: Int = 40_000
) {
    init {
        require(numColors >= 2) { "Need >= 2 colors" }
        require(bottleCapacity >= 2) { "Capacity must be >= 2" }
    }
}

fun emptyBottleCount(d: Difficulty, numColors: Int) = when (d) {
    Difficulty.EASY -> maxOf(2, numColors / 3)
    Difficulty.MEDIUM -> maxOf(2, numColors / 4)
    Difficulty.HARD -> maxOf(1, numColors / 6)
    Difficulty.EXPERT -> 1
}

fun targetMoveRange(d: Difficulty, numColors: Int): IntRange {
    val base = numColors
    return when (d) {
        Difficulty.EASY -> base..(base * 2)
        Difficulty.MEDIUM -> (base * 2)..(base * 3)
        Difficulty.HARD -> (base * 3)..(base * 4)
        Difficulty.EXPERT -> (base * 4)..(base * 6)
    }
}





fun PuzzleState.toBottles(palette: List<Color>, capacity: Int): List<Bottle> =
    mapIndexed { id, contents -> Bottle(id, capacity, contents.map { palette[it] }.toMutableStateList()) }