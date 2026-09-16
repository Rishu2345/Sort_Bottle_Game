package com.buildsol.bottolshort.game.presentation.action

import com.buildsol.bottolshort.game.data.LevelConfig

sealed interface GameAction {
    data class SelectBottle(val index: Int) : GameAction
    data object Reset : GameAction
    data class NewGame(val config: LevelConfig) : GameAction
}
