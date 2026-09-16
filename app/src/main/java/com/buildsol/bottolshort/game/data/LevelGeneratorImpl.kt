package com.buildsol.bottolshort.game.data

import com.buildsol.bottolshort.game.domain.model.Bottle
import com.buildsol.bottolshort.game.domain.model.Color
import com.buildsol.bottolshort.game.domain.repository.LevelGenerator
import kotlin.collections.chunked
import kotlin.random.Random


class LevelGeneratorImpl : LevelGenerator {

    override suspend fun generate(config: LevelConfig): List<Bottle> {
        val rng = config.seed?.let { Random(it) } ?: Random(System.nanoTime())
        val palette = Color.entries.take(config.numColors)
        require(palette.size == config.numColors) {
            "Only ${Color.entries.size} colors defined; requested ${config.numColors}. Add more entries to Color."
        }
        val emptyBottles = emptyBottleCount(config.difficulty, config.numColors)
        val wantedRange = targetMoveRange(config.difficulty, config.numColors)

        var fallback: PuzzleState? = null

        repeat(config.maxAttempts) {
            val candidate = randomDistribution(rng, config.numColors, config.bottleCapacity, emptyBottles)
            when (val r = PuzzleSolver.solve(candidate, config.bottleCapacity, config.solverNodeBudget)) {
                is PuzzleSolver.Result.Solved -> {
                    if (r.moveCount in wantedRange) return candidate.toBottles(palette, config.bottleCapacity)
                    if (fallback == null) fallback = candidate
                }
                PuzzleSolver.Result.NotFound -> Unit
            }
        }
        val safe =
            fallback ?: ((0 until config.numColors).map { c -> List(config.bottleCapacity) { c } } +
                    List(emptyBottles) { emptyList() })
        return safe.toBottles(palette, config.bottleCapacity)
    }

    private fun randomDistribution(rng: Random, numColors: Int, capacity: Int, emptyBottles: Int): PuzzleState {
        val units = (0 until numColors).flatMap { c -> List(capacity) { c } }.shuffled(rng)
        return units.chunked(capacity) + List(emptyBottles) { emptyList() }
    }
}