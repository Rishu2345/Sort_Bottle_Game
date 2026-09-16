package com.buildsol.bottolshort.game.domain.repository

import com.buildsol.bottolshort.game.data.LevelConfig
import com.buildsol.bottolshort.game.domain.model.Bottle

interface LevelGenerator {
        suspend fun generate(config: LevelConfig): List<Bottle>
}
