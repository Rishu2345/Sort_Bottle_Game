package com.buildsol.bottolshort.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.buildsol.bottolshort.core.presentation.ObserveAsEvents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun <E> ObserveAsEvents(
    events: Flow<E>,
    onEvent: (E) -> Unit
) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    androidx.compose.runtime.LaunchedEffect(events, lifecycleOwner) {
        events.collect { ev -> onEvent(ev) }
    }
}
