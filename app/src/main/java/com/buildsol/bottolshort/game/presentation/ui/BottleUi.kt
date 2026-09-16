package com.buildsol.bottolshort.game.presentation.ui

import com.buildsol.bottolshort.ui.theme.liquidCoral
import com.buildsol.bottolshort.ui.theme.liquidDustyBlue
import com.buildsol.bottolshort.ui.theme.liquidMustard
import com.buildsol.bottolshort.ui.theme.liquidOchre
import com.buildsol.bottolshort.ui.theme.liquidPlum
import com.buildsol.bottolshort.ui.theme.liquidSage
import com.buildsol.bottolshort.ui.theme.liquidTeal
import com.buildsol.bottolshort.ui.theme.liquidTerracotta
import androidx.compose.ui.graphics.Color as ComposeColor
import com.buildsol.bottolshort.game.domain.model.Color as ModelColor

data class BottleUi(
    val id: Int,
    val colors: List<ComposeColor>,
    val capacity: Int = 4
) {
    val isSolved: Boolean get() = colors.isEmpty() || (colors.all { it == colors.first() } && colors.size == capacity)
}

fun com.buildsol.bottolshort.game.domain.model.Bottle.toBottleUi(): BottleUi {
    return BottleUi(
        id = this.id,
        colors = this.layers.map { modelColorToCompose(it) },
        capacity = this.capacity
    )
}

private fun modelColorToCompose(color: ModelColor): ComposeColor = when (color) {
    ModelColor.CORAL -> liquidCoral
    ModelColor.TEAL -> liquidTeal
    ModelColor.OCHRE -> liquidOchre
    ModelColor.PLUM -> liquidPlum
    ModelColor.SAGE -> liquidSage
    ModelColor.TERRACOTTA -> liquidTerracotta
    ModelColor.MUSTARD -> liquidMustard
    ModelColor.DUSTY_BLUE -> liquidDustyBlue
}