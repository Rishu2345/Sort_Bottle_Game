package com.buildsol.bottolshort.game.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.buildsol.bottolshort.core.domain.EmptyResult
import com.buildsol.bottolshort.core.domain.Result
import com.buildsol.bottolshort.game.data.datastore.DataStoreKey
import com.buildsol.bottolshort.game.domain.repository.Settings
import com.buildsol.bottolshort.game.domain.repository.SettingsWriteError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : Settings {

    override fun observeLevelConfig(): Flow<LevelConfig> {
        return dataStore.data.map { preferences ->
            val numColors = preferences[DataStoreKey.NUM_COLOR] ?: DefaultLevelConfig.value.numColors
            val bottleCapacity = preferences[DataStoreKey.BOTTLE_CAPACITY]
                ?: DefaultLevelConfig.value.bottleCapacity
            val difficultyString = preferences[DataStoreKey.DIFFICULTY]
                ?: DefaultLevelConfig.value.difficulty.name
            val difficulty = Difficulty.entries.find { it.name == difficultyString }
                ?: DefaultLevelConfig.value.difficulty
            val hasCustomSeed = preferences[DataStoreKey.IS_CUSTOM_SEED] ?: false
            val seed = if (hasCustomSeed) {
                preferences[DataStoreKey.SEED] ?: DefaultLevelConfig.value.seed
            } else {
                null
            }
            LevelConfig(
                numColors = numColors,
                bottleCapacity = bottleCapacity,
                difficulty = difficulty,
                seed = seed
            )
        }.catch { e ->
            emit(DefaultLevelConfig.value)
        }
    }

    override suspend fun saveLevelConfig(config: LevelConfig): EmptyResult<SettingsWriteError> {
        return try {
            dataStore.edit { preferences ->
                preferences[DataStoreKey.NUM_COLOR] = config.numColors
                preferences[DataStoreKey.BOTTLE_CAPACITY] = config.bottleCapacity
                preferences[DataStoreKey.DIFFICULTY] = config.difficulty.name
                preferences[DataStoreKey.IS_CUSTOM_SEED] = config.seed != null
                preferences[DataStoreKey.SEED] = config.seed ?: 0
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(SettingsWriteError.UNKNOWN)
        }
    }
}
