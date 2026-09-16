package com.buildsol.bottolshort.game.presentation.state

import com.buildsol.bottolshort.game.presentation.ui.BottleUi

data class GameState(
    val bottles: List<BottleUi> = emptyList(),
    val selectedIndex: Int? = null,
    val moves: Int = 0,
    val isWin: Boolean = false,
    val error: String? = null,
    val loading:Boolean = false,
    val nextGamesBottol:List<BottleUi>? = null,
)
