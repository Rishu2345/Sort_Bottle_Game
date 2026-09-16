package com.buildsol.bottolshort

import android.app.Application
import org.koin.core.context.startKoin
import org.koin.dsl.module
import com.buildsol.bottolshort.core.di.GameModule
import org.koin.android.ext.koin.androidContext

class BottolShortApp : Application() {
    override fun onCreate() {
        super.onCreate()
                startKoin {
            androidContext(this@BottolShortApp)
            modules(listOf(GameModule.module))
        }
    }
}
