package com.buildsol.bottolshort.game.domain.model

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.mutableStateListOf

data class Bottle(
    val id: Int,
    val capacity: Int = 4,
    val layers: SnapshotStateList<Color> = mutableStateListOf()
) {
    val isEmpty: Boolean get() = layers.isEmpty()
    val isFull: Boolean get() = layers.size >= capacity
    val topColor: Color? get() = layers.lastOrNull()
    val isSolved: Boolean get() =
        layers.isEmpty() || (layers.size == capacity && layers.all { it == layers[0] })
}
