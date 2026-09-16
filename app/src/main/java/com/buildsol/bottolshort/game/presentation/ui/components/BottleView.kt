package com.buildsol.bottolshort.game.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.buildsol.bottolshort.game.presentation.ui.BottleUi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.tooling.preview.Preview
import com.buildsol.bottolshort.ui.theme.liquidCoral
import com.buildsol.bottolshort.ui.theme.liquidOchre
import com.buildsol.bottolshort.ui.theme.liquidPlum
import com.buildsol.bottolshort.ui.theme.liquidTeal


@Composable
fun BottleViewP(
    bottle: BottleUi,
    isSelected: Boolean,
    onClick: () -> Unit
) {
        Card(
        modifier = Modifier
            .size(width = 80.dp, height = 120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else BorderStroke(1.dp, Color.Gray)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(4.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
                        repeat(bottle.capacity - bottle.colors.size) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(Color.LightGray.copy(alpha = 0.2f))
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
                        bottle.colors.reversed().forEach { layerColor ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(layerColor)
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

        }
    }
}



@Composable
fun BottleViewpp(
    bottle: BottleUi,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = MaterialTheme.colorScheme.primary
    val liftOffset by animateFloatAsState(
        targetValue = if (isSelected) -12f else 0f,
        animationSpec = tween(durationMillis = 150),
        label = "bottleLift"
    )

    Canvas(
        modifier = Modifier
            .width(64.dp)
            .height(140.dp)
            .clickable { onClick() }
    ) {
        val w = size.width
        val h = size.height

        val neckWidth = w * 0.42f
        val neckHeight = h * 0.16f
        val bodyTop = neckHeight
        val bodyHeight = h - bodyTop
        val cornerRadius = w * 0.22f

        val yOffset = liftOffset

                val flaskPath = Path().apply {
                        val neckLeft = (w - neckWidth) / 2f
            moveTo(neckLeft, yOffset)
            lineTo(neckLeft + neckWidth, yOffset)
            lineTo(neckLeft + neckWidth, bodyTop + yOffset)

                        addRoundRect(
                androidx.compose.ui.geometry.RoundRect(
                    left = 0f,
                    top = bodyTop + yOffset,
                    right = w,
                    bottom = h + yOffset,
                    topLeftCornerRadius = CornerRadius(cornerRadius * 0.3f, cornerRadius * 0.3f),
                    topRightCornerRadius = CornerRadius(cornerRadius * 0.3f, cornerRadius * 0.3f),
                    bottomLeftCornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    bottomRightCornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            )
        }

                if (isSelected) {
            drawPath(
                path = flaskPath,
                color = borderColor.copy(alpha = 0.15f),
                style = Stroke(width = 14f)
            )
        }

                clipPath(flaskPath) {
            val layerHeight = bodyHeight / bottle.capacity
            bottle.colors.forEachIndexed { index, layerColor ->
                val layerTop = (h + yOffset) - (index + 1) * layerHeight
                drawRect(
                    color = layerColor,
                    topLeft = Offset(0f, layerTop),
                    size = Size(w, layerHeight)
                )
            }

                        drawRect(
                color = Color.White.copy(alpha = 0.06f),
                topLeft = Offset(0f, yOffset),
                size = Size(w, bodyHeight - bottle.colors.size * layerHeight)
            )
        }

                drawPath(
            path = flaskPath,
            color = if (isSelected) borderColor else Color.Gray.copy(alpha = 0.6f),
            style = Stroke(width = if (isSelected) 5f else 3f)
        )

                if (bottle.colors.isNotEmpty()) {
            val layerHeight = bodyHeight / bottle.capacity
            val surfaceY = (h + yOffset) - bottle.colors.size * layerHeight
            drawRect(
                color = Color.White.copy(alpha = 0.25f),
                topLeft = Offset(w * 0.08f, surfaceY),
                size = Size(w * 0.84f, 3f)
            )
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.clipPath(
    path: Path,
    block: androidx.compose.ui.graphics.drawscope.DrawScope.() -> Unit
) {
    clipPath(path = path, clipOp = androidx.compose.ui.graphics.ClipOp.Intersect) {
        block()
    }
}

@Composable
@Preview
fun BottleViewPV(
    modifier: Modifier = Modifier,
    layers: List<Color> = listOf(liquidPlum, liquidTeal, liquidCoral, liquidOchre)
) {
    Canvas(
        modifier = modifier
            .width(60.dp)
            .height(225.dp)
    ) {
        val bottleWidth = size.width
        val bottleHeight = size.height

                val bottlePath = Path().apply {
            moveTo(10f, 1f)
            lineTo(50f, 1f)

            quadraticTo(
                58f, 1f,
                58f, 9f
            )

            lineTo(58f, 193f)

            cubicTo(
                58f, 202f,
                51f, 209f,
                42f, 209f
            )

            lineTo(18f, 209f)

            cubicTo(
                9f, 209f,
                2f, 202f,
                2f, 193f
            )

            lineTo(2f, 9f)

            quadraticTo(
                2f, 1f,
                10f, 1f
            )

            close()
        }

                drawPath(
            path = bottlePath,
            color = Color.White.copy(alpha = 0.8f)
        )

                clipPath(bottlePath) {

            val liquidLeft = 6f
            val liquidRight = size.width - 6f

            val liquidTop = 29f
            val layerHeight = 44f

            layers.forEachIndexed { index, color ->

                val top = liquidTop +
                        (layers.size - 1 - index) * layerHeight

                drawRect(
                    color = color,
                    topLeft = Offset(liquidLeft, top),
                    size = Size(
                        liquidRight - liquidLeft,
                        layerHeight
                    )
                )
            }

                        drawRoundRect(
                color = Color.White.copy(alpha = 0.2f),
                topLeft = Offset(6f, 31f),
                size = Size(48f, 4f),
                cornerRadius = CornerRadius(2f)
            )
        }

                drawRoundRect(
            color = Color(0xFFEBE8E3).copy(alpha = 0.6f),
            topLeft = Offset(10f, 5f),
            size = Size(40f, 6f),
            cornerRadius = CornerRadius(3f)
        )
    }
}