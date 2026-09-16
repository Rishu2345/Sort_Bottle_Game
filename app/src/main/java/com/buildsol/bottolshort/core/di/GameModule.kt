package com.buildsol.bottolshort.core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.buildsol.bottolshort.game.data.LevelGeneratorImpl
import com.buildsol.bottolshort.game.data.SettingsRepositoryImpl
import com.buildsol.bottolshort.game.data.datastore.DataStore
import com.buildsol.bottolshort.game.data.datastore.DataStoreHelper
import com.buildsol.bottolshort.game.domain.repository.LevelGenerator
import com.buildsol.bottolshort.game.domain.repository.Settings
import com.buildsol.bottolshort.game.presentation.viewmodel.BottleGameViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object GameModule {
    val module = module {
                single<DataStore<Preferences>> { DataStore(androidContext()) }
        single<DataStoreHelper> { DataStoreHelper(get()) }
        single<Settings> { SettingsRepositoryImpl(get()) }
        single<LevelGenerator> { LevelGeneratorImpl() }

                viewModelOf(::BottleGameViewModel)
    }
}
