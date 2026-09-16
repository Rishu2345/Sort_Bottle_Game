package com.buildsol.bottolshort.game.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.buildsol.bottolshort.game.data.Difficulty
import com.buildsol.bottolshort.game.data.LevelConfig

private val OnSurfaceMuted = Color(0xFF8C877E)
private val StepperTrack = Color(0xFFEFEBE4)
private val SegmentTrack = Color(0xFFEFEBE4)

@Composable
fun SettingsDialog(
    currentConfig: LevelConfig,
    onDismiss: () -> Unit,
    onApply: (LevelConfig) -> Unit
) {
    var numColors by remember { mutableIntStateOf(currentConfig.numColors) }
    var bottleCapacity by remember { mutableIntStateOf(currentConfig.bottleCapacity) }
    var difficulty by remember { mutableStateOf(currentConfig.difficulty) }
    var useCustomSeed by remember { mutableStateOf(currentConfig.seed != null) }
    var seedText by remember { mutableStateOf(currentConfig.seed?.toString() ?: "") }
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier,
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            tonalElevation = 0.dp
        ) {
            Column(modifier = Modifier.padding(24.dp)) {

                                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Puzzle Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2B2A28)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = OnSurfaceMuted)
                    }
                }

                Spacer(Modifier.height(20.dp))

                                SettingStepperRow(
                    title = "Number of Colors",
                    subtitle = "Range 2–6",
                    value = numColors,
                    onDecrement = { if (numColors > 2) numColors-- },
                    onIncrement = { if (numColors < 6) numColors++ }
                )

                Spacer(Modifier.height(20.dp))

                                SettingStepperRow(
                    title = "Bottle Capacity",
                    subtitle = "Segments per bottle",
                    value = bottleCapacity,
                    onDecrement = { if (bottleCapacity > 2) bottleCapacity-- },
                    onIncrement = { if (bottleCapacity < 6) bottleCapacity++ }
                )

                Spacer(Modifier.height(24.dp))

                                Text(
                    text = "Difficulty",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2B2A28)
                )
                Spacer(Modifier.height(10.dp))
                DifficultySegmentedRow(
                    selected = difficulty,
                    onSelect = { difficulty = it }
                )

                Spacer(Modifier.height(24.dp))

                                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Use custom seed",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2B2A28)
                    )
                    Switch(
                        checked = useCustomSeed,
                        onCheckedChange = { useCustomSeed = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF2B2A28),
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color(0xFFD8D3C9)
                        )
                    )
                }

                                AnimatedVisibility(
                    visible = useCustomSeed,
                    enter = expandVertically(tween()),
                    exit = shrinkVertically(tween())
                ) {
                    Column {
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Seed (numeric)",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceMuted
                        )
                        Spacer(Modifier.height(6.dp))
                        OutlinedTextField(
                            value = seedText,
                            onValueChange = { new -> if (new.all { it.isDigit() }) seedText = new },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF2B2A28),
                                unfocusedBorderColor = Color(0xFFD8D3C9),
                                focusedContainerColor = Color(0xFFF9F7F3),
                                unfocusedContainerColor = Color(0xFFF9F7F3)
                            )
                        )
                    }
                }

                Spacer(Modifier.height(28.dp))

                                Button(
                    onClick = {
                        val config = LevelConfig(
                            numColors = numColors,
                            bottleCapacity = bottleCapacity,
                            difficulty = difficulty,
                            seed = if (useCustomSeed) seedText.toLongOrNull() else null
                        )
                        onApply(config)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2B2A28),
                        contentColor = Color.White
                    )
                ) {
                    Text("Apply & New Game", fontWeight = FontWeight.Medium)
                }

                Spacer(Modifier.height(12.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel", color = OnSurfaceMuted)
                }
            }
        }
    }
}

@Composable
private fun SettingStepperRow(
    title: String,
    subtitle: String,
    value: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2B2A28)
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceMuted
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            StepperButton(icon = Icons.Default.Remove, onClick = onDecrement)
            Text(
                text = value.toString(),
                modifier = Modifier.padding(horizontal = 14.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2B2A28)
            )
            StepperButton(icon = Icons.Default.Add, onClick = onIncrement)
        }
    }
}

@Composable
private fun StepperButton(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .size(32.dp)
            .background(StepperTrack, CircleShape)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF2B2A28), modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun DifficultySegmentedRow(
    selected: Difficulty,
    onSelect: (Difficulty) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SegmentTrack, RoundedCornerShape(14.dp))
            .padding(4.dp)
    ) {
        Difficulty.entries.forEach { option ->
            val isSelected = option == selected
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSelect(option) }
                    .background(
                        color = if (isSelected) Color(0xFF2B2A28) else Color.Transparent,
                        shape = RoundedCornerShape(11.dp)
                    )
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = option.name.lowercase().replaceFirstChar { it.uppercase() },
                    color = if (isSelected) Color.White else Color(0xFF6B665D),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

