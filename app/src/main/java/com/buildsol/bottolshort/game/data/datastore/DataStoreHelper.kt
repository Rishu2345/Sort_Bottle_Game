package com.buildsol.bottolshort.game.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.text.get

class DataStoreHelper(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun <T> put(
        key: Preferences.Key<T>,
        value: T
    ) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    fun <T> get(
        key: Preferences.Key<T>,
        default :T
    ): Flow<T> {
        return dataStore.data.map { preferences ->
            preferences[key] ?: default
        }
    }


}