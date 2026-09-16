package com.buildsol.bottolshort.game.domain.repository

import com.buildsol.bottolshort.core.domain.EmptyResult
import com.buildsol.bottolshort.game.data.LevelConfig
import kotlinx.coroutines.flow.Flow

interface Settings {
    fun observeLevelConfig(): Flow<LevelConfig>
    suspend fun saveLevelConfig(config: LevelConfig): EmptyResult<SettingsWriteError>
}

enum class SettingsWriteError : com.buildsol.bottolshort.core.domain.Error {
    UNKNOWN
}
