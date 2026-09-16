package com.buildsol.bottolshort.game.presentation.event

sealed interface GameEvent {
    data object ShowWinDialog : GameEvent
}
