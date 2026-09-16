package com.buildsol.bottolshort.game.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.buildsol.bottolshort.game.presentation.ui.BottleUi


private val BottleShape = RoundedCornerShape(
    topStartPercent = 13,
    topEndPercent = 13,
    bottomStartPercent = 27,
    bottomEndPercent = 27
)

private const val NECK_TOP_FRACTION = 5f / 209f
private const val NECK_HEIGHT_FRACTION = 6f / 209f
private const val NECK_WIDTH_START_FRACTION = 10f / 60f
private const val NECK_WIDTH_FRACTION = 40f / 60f

private const val LIQUID_TOP_FRACTION = 29f / 209f
private const val LIQUID_BOTTOM_FRACTION = 193f / 209f

private const val GLASS_WIDTH = 15f

@Composable
fun BottleView(
    bottle: BottleUi,
    isSelected: Boolean,
    onClick: () -> Unit,
    width: Dp = 60.dp,
    height: Dp = 209.dp
) {
    val primary = MaterialTheme.colorScheme.primary
    val liftOffset by animateFloatAsState(
        targetValue = if (isSelected) -50f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "bottleLift"
    )

    Box{
        Box(
            modifier = Modifier
                .width(width)
                .height(height)
                .graphicsLayer { translationY = liftOffset }
                .shadow(elevation = 1.dp, shape = BottleShape, clip = false)
                .clip(BottleShape)
                .background(Color.White.copy(alpha = 0.8f))
                .border(
                    width = 1.dp,
                    color = if (isSelected) primary else Color.Gray.copy(alpha = 0.25f),
                    shape = BottleShape
                )
                .clickable { onClick() }
        ) {
            Canvas(modifier = Modifier
                .fillMaxSize()) {
                val w = size.width
                val h = size.height

                val liquidTop = h * LIQUID_TOP_FRACTION
                val liquidBottom = h - 20f
                val liquidHeight = liquidBottom - liquidTop
                val layerHeight = liquidHeight / bottle.capacity

                bottle.colors.forEachIndexed { index, layerColor ->
                    val layerTop = liquidBottom - (index + 1) * layerHeight
                    if(index == 0){
                        drawRect(
                            color = layerColor,
                            topLeft = Offset(GLASS_WIDTH, layerTop),
                            size = Size(
                                w - 2 * (GLASS_WIDTH),
                                layerHeight - 25f + 1f
                            )
                        drawRoundRect(
                            cornerRadius = CornerRadius(60f),
                            color = layerColor,
                            topLeft = Offset(GLASS_WIDTH, (layerTop + (layerHeight - 50f ))),
                            size = Size(w-2*(GLASS_WIDTH), 50f)
                        )
                    }
                    else{
                        drawRect(
                            color = layerColor,
                            topLeft = Offset(GLASS_WIDTH, layerTop),
                            size = Size(
                                w - 2 * (GLASS_WIDTH),
                                layerHeight + 1f
                            )
                        )
                    }
                }

                if (bottle.colors.isNotEmpty()) {
                    val surfaceY = liquidBottom - bottle.colors.size * layerHeight + 5f
                    val stripHeight = h * 0.019f
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.4f),
                        topLeft = Offset(w * 0.2f, surfaceY),
                        size = Size(w * 0.6f, stripHeight),
                        cornerRadius = CornerRadius(stripHeight / 2, stripHeight / 2)
                    )
                }

                val neckHeight = h * NECK_HEIGHT_FRACTION
                drawRoundRect(
                    color = Color(0xFFC4C0C0).copy(alpha = 0.6f),
                    topLeft = Offset(w * NECK_WIDTH_START_FRACTION, h * NECK_TOP_FRACTION),
                    size = Size(w * NECK_WIDTH_FRACTION, neckHeight),
                    cornerRadius = CornerRadius(neckHeight / 2, neckHeight / 2)
                )
            }
        }
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            visible = isSelected,
            enter = scaleIn(
                tween(300)
            ),
            exit = scaleOut(tween(300))
        ){
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(Color.Black, CircleShape)

            )
        }
    }
}




@Preview(
    name = "Bottle - Mixed Colors",
    showBackground = true,
    backgroundColor = 0xFFF5F1EA
)
@Composable
private fun BottleViewPreview() {
    MaterialTheme {
        BottleView(
            bottle = BottleUi(
                id = 1,
                colors = listOf(
                    Color(0xFFE57373),
                    Color(0xFF64B5F6),
                    Color(0xFFFFD54F),
                    Color(0xFF81C784)
                )
            ),
            isSelected = false,
            onClick = {}
        )
    }
}

@Preview(
    name = "Bottle States",
    showBackground = true,
    backgroundColor = 0xFFF5F1EA,
    widthDp = 360,
    heightDp = 700
)
@Composable
private fun BottleStatesPreview() {
    MaterialTheme {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottleView(
                bottle = BottleUi(
                    id = 1,
                    colors = emptyList()
                ),
                isSelected = true,
                onClick = {}
            )

            BottleView(
                bottle = BottleUi(
                    id = 2,
                    colors = listOf(
                        Color(0xFFE57373),
                        Color(0xFFE57373)
                    )
                ),
                isSelected = false,
                onClick = {}
            )

            BottleView(
                bottle = BottleUi(
                    id = 3,
                    colors = listOf(
                        Color(0xFFE57373),
                        Color(0xFF64B5F6),
                        Color(0xFFFFD54F),
                        Color(0xFF81C784)
                    )
                ),
                isSelected = true,
                onClick = {}
            )
        }
    }
}