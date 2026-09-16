package com.buildsol.bottolshort.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.buildsol.bottolshort.game.presentation.ui.BottleSortRoot
import com.buildsol.bottolshort.game.presentation.ui.StartScreen

@Composable
fun BottleSortNavHost() {
    val backStack = rememberNavBackStack(Screen.Start)
    NavDisplay(
        backStack = backStack,
        onBack = {
            backStack.removeLastOrNull()
        },
        entryProvider = entryProvider {
            entry<Screen.Start> {
                StartScreen(
                    onPlayClick = {backStack.add(Screen.Game)}
                )
            }
            entry<Screen.Game> {
                BottleSortRoot()
            }
        }
    )
}
