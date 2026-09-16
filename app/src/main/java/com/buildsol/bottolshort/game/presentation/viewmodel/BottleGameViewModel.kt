package com.buildsol.bottolshort.game.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildsol.bottolshort.game.data.DefaultLevelConfig
import com.buildsol.bottolshort.game.data.LevelConfig
import com.buildsol.bottolshort.game.domain.repository.LevelGenerator
import com.buildsol.bottolshort.game.presentation.action.GameAction
import com.buildsol.bottolshort.game.presentation.event.GameEvent
import com.buildsol.bottolshort.game.presentation.state.GameState
import com.buildsol.bottolshort.game.presentation.ui.toBottleUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.min
import com.buildsol.bottolshort.game.domain.repository.Settings
import com.buildsol.bottolshort.core.domain.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class BottleGameViewModel(
    private val levelGenerator: LevelGenerator,
    private val settings: Settings,
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    private val _events = Channel<GameEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _currentConfig = MutableStateFlow(DefaultLevelConfig.value)
    val currentConfig = _currentConfig.asStateFlow()

    private var nextGameJob : Job? = null

    init {
        viewModelScope.launch(Dispatchers.Default) {
            settings.observeLevelConfig().collect { config ->
                nextGameJob?.cancel()
                nextGameJob = null
                _currentConfig.value = config
                _state.update {
                    it.copy(nextGamesBottol = null)
                }
                _currentConfig.value = config
                reloadLevel(config)
            }
        }
    }


    private suspend fun reloadLevel(config: LevelConfig) {
        _state.update { it.copy(loading = true) }
        nextGameJob?.join()

        val nextBottles = _state.value.nextGamesBottol

        if (nextBottles != null) {
            _state.update {
                it.copy(
                    bottles = nextBottles,
                    nextGamesBottol = null,
                    loading = false,
                    moves = 0,
                    selectedIndex = null,
                    isWin = false
                )
            }
        } else {

            val bottles = levelGenerator.generate(config).map { it.toBottleUi() }

            _state.update {
                it.copy(
                    bottles = bottles,
                    loading = false,
                    moves = 0,
                    selectedIndex = null,
                    isWin = false
                )
            }
        }
        generateNextLevel()
    }


    private fun generateNextLevel() {
        if (_state.value.nextGamesBottol != null) return
        if (nextGameJob?.isActive == true) return

        nextGameJob = viewModelScope.launch(Dispatchers.Default) {
            try {
                val config = currentConfig.value

                val bottles = levelGenerator
                    .generate(config)
                    .map { it.toBottleUi() }

                _state.update {
                    if (currentConfig.value == config) {
                        it.copy(nextGamesBottol = bottles)
                    } else {
                        it
                    }
                }
            } finally {
                nextGameJob = null
            }
        }
    }



    fun onAction(action: GameAction) {
        when (action) {
            is GameAction.SelectBottle -> handleSelect(action.index)
            GameAction.Reset ->
                viewModelScope.launch(Dispatchers.Default) { reloadLevel(currentConfig.value)}
            is GameAction.NewGame -> {
                viewModelScope.launch(Dispatchers.Default) {
                    when (settings.saveLevelConfig(action.config)) {
                        is Result.Success -> {
                            _currentConfig.update{ action.config}
                            reloadLevel(action.config)
                        }
                        is Result.Failure -> {
                                                    }
                    }
                }
            }
        }
    }

    private fun handleSelect(index: Int) {
        val current = _state.value
        when (current.selectedIndex) {
            null -> {
                if (!current.bottles[index].colors.isEmpty()) {
                    _state.value = current.copy(selectedIndex = index)
                }
            }
            else -> {
                                val srcIdx = current.selectedIndex
                val dstIdx = index
                if (srcIdx == dstIdx) {
                    _state.value = current.copy(selectedIndex = null)
                    return
                }
                if (attemptPour(srcIdx, dstIdx)) {
                    _state.value = _state.value.copy(moves = current.moves + 1, selectedIndex = null)
                    checkWin()
                } else {
                    _state.value = current.copy(selectedIndex = null)
                }
            }
        }
    }

        private fun attemptPour(srcIdx: Int, dstIdx: Int): Boolean {
        val bottles = _state.value.bottles.toMutableList()
        val src = bottles[srcIdx]
        val dst = bottles[dstIdx]
        if (src.colors.isEmpty()) return false
        if (dst.colors.size >= dst.capacity) return false
        val topColor = src.colors.last()
        if (dst.colors.isNotEmpty() && dst.colors.last() != topColor) return false
        var movable = 0
        for (i in src.colors.indices.reversed()) {
            if (src.colors[i] == topColor) movable++ else break
        }
        val space = dst.capacity - dst.colors.size
        val toMove = min(movable, space)
        if (toMove == 0) return false
        val newSrcColors = src.colors.subList(0, src.colors.size - toMove)
        val newDstColors = dst.colors + src.colors.subList(src.colors.size - toMove, src.colors.size)
        bottles[srcIdx] = src.copy(colors = newSrcColors)
        bottles[dstIdx] = dst.copy(colors = newDstColors)
        _state.value = _state.value.copy(bottles = bottles)
        return true
    }

    private fun checkWin() {
        if (_state.value.bottles.all { it.isSolved }) {
            _state.value = _state.value.copy(isWin = true)
            viewModelScope.launch { _events.send(GameEvent.ShowWinDialog) }
        }
    }
}
