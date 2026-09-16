package com.buildsol.bottolshort.game.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.buildsol.bottolshort.core.presentation.ObserveAsEvents
import com.buildsol.bottolshort.game.data.LevelConfig
import com.buildsol.bottolshort.game.presentation.action.GameAction
import com.buildsol.bottolshort.game.presentation.event.GameEvent
import com.buildsol.bottolshort.game.presentation.state.GameState
import com.buildsol.bottolshort.game.presentation.ui.components.BottleView
import com.buildsol.bottolshort.game.presentation.ui.components.LoadingDialog
import com.buildsol.bottolshort.game.presentation.ui.components.SettingsDialog
import com.buildsol.bottolshort.game.presentation.ui.components.WinDialog
import com.buildsol.bottolshort.game.presentation.viewmodel.BottleGameViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun BottleSortRoot(
    viewModel: BottleGameViewModel = koinViewModel(),
    onBackClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val currentConfig by viewModel.currentConfig.collectAsState()
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is GameEvent.ShowWinDialog -> {
                            }
        }
    }
    BottleSortScreen(
        state = state,
        currentConfig = currentConfig,
        onAction = viewModel::onAction,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottleSortScreen(
    state: GameState,
    currentConfig: LevelConfig,
    onAction: (GameAction) -> Unit,
    onBackClick: () -> Unit = {}
) {
    var showSettings by remember { mutableStateOf(false) }
    LoadingDialog(state.loading)
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bottle Sort",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    IconButton(
                        onClick = { showSettings = true },
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(28.dp)
                            .background(MaterialTheme.colorScheme.onBackground, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.background,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(GameAction.Reset) },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                MoveCounter(moves = state.moves)

                Spacer(modifier = Modifier.height(28.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        14.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    state.bottles.forEachIndexed { index, bottle ->
                        BottleView(
                            bottle = bottle,
                            isSelected = state.selectedIndex == index,
                            onClick = { onAction(GameAction.SelectBottle(index)) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                HintRow()
            }

            if (state.isWin) {
                WinDialog(
                    moves = state.moves,
                    onDismiss = { onAction(GameAction.Reset) }
                )
            }

            if (showSettings) {
                SettingsDialog(
                    currentConfig = currentConfig,
                    onDismiss = { showSettings = false },
                    onApply = { config ->
                        showSettings = false
                        onAction(GameAction.NewGame(config))
                    }
                )
            }
        }
    }
}

@Composable
private fun MoveCounter(moves: Int) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "MOVES",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "$moves",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun HintRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "Tap two bottles to move colors between them",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}