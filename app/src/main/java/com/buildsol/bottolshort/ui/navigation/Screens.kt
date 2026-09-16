package com.buildsol.bottolshort.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen: NavKey {
    @Serializable object Start : Screen
    @Serializable object Game : Screen
}
