package com.buildsol.bottolshort.game.data.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.buildsol.bottolshort.game.data.Difficulty

object DataStoreKey {
    val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    val NUM_COLOR = intPreferencesKey("color_number")
    val BOTTLE_CAPACITY = intPreferencesKey("bottle_capacity")
    val DIFFICULTY = stringPreferencesKey("levelDifficulty")
    val IS_CUSTOM_SEED = booleanPreferencesKey("custom_seed")
    val SEED = longPreferencesKey("seed")
}


