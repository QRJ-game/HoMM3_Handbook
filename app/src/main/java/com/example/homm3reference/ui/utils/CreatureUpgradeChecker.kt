package com.example.homm3reference.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homm3reference.R
import com.example.homm3reference.ui.common.AppBackground
import com.example.homm3reference.ui.theme.HommGlassBackground
import com.example.homm3reference.ui.theme.HommGold
import com.example.homm3reference.ui.theme.HommWhite
import kotlin.math.floor

@Composable
fun CreatureUpgradeCheckerScreen() {
    var xCoord by remember { mutableStateOf("") }
    var yCoord by remember { mutableStateOf("") }
    var isDungeon by remember { mutableStateOf(false) } // false = Поверхность (z=0), true = Подземелье (z=1)
    var resultText by remember { mutableStateOf("") }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Заголовок
            Text(
                text = stringResource(R.string.utility_upgrade_check),
                color = HommGold,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp, top = 32.dp)
            )

            // Контейнер формы
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HommGlassBackground, RoundedCornerShape(8.dp))
                    .border(2.dp, HommGold, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    // Поле ввода X
                    HommTextField(
                        value = xCoord,
                        onValueChange = { if (it.all { char -> char.isDigit() }) xCoord = it },
                        label = stringResource(R.string.calc_coord_x)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Поле ввода Y
                    HommTextField(
                        value = yCoord,
                        onValueChange = { if (it.all { char -> char.isDigit() }) yCoord = it },
                        label = stringResource(R.string.calc_coord_y)
                    )

                    // Переключатель Поверхность / Подземелье
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterChip(
                            selected = !isDungeon,
                            onClick = { isDungeon = false },
                            label = { Text(stringResource(R.string.calc_surface)) },
                            enabled = true, // Явное указание параметра
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = HommGold,
                                selectedLabelColor = Color.Black,
                                containerColor = Color.Transparent,
                                labelColor = HommWhite
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = HommGold,
                                selectedBorderColor = HommGold,
                                borderWidth = 2.dp,
                                selectedBorderWidth = 2.dp,
                                enabled = true,
                                selected = !isDungeon
                            )
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        FilterChip(
                            selected = isDungeon,
                            onClick = { isDungeon = true },
                            label = { Text(stringResource(R.string.calc_dungeon)) },
                            enabled = true, // Явное указание параметра
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = HommGold,
                                selectedLabelColor = Color.Black,
                                containerColor = Color.Transparent,
                                labelColor = HommWhite
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = HommGold,
                                selectedBorderColor = HommGold,
                                borderWidth = 2.dp,
                                selectedBorderWidth = 2.dp,
                                enabled = true,
                                selected = isDungeon
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Кнопка Расчет
                    Button(
                        onClick = {
                            if (xCoord.isNotEmpty() && yCoord.isNotEmpty()) {
                                resultText = calculateUpgrade(
                                    xCoord.toIntOrNull() ?: 0,
                                    yCoord.toIntOrNull() ?: 0,
                                    if (isDungeon) 1 else 0
                                )
                            } else {
                                resultText = "Введите координаты"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = HommGold),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text(
                            text = stringResource(R.string.calc_button),
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Результат
            if (resultText.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(HommGlassBackground, RoundedCornerShape(8.dp))
                        .border(2.dp, HommGold, RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = resultText,
                        color = if (resultText.contains("ЕСТЬ") || resultText.contains("YES")) Color.Green
                        else if (resultText.contains("НЕТ") || resultText.contains("NO")) Color.Red
                        else HommWhite,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun HommTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = HommGold) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = HommWhite,
            unfocusedTextColor = HommWhite,
            focusedBorderColor = HommGold,
            unfocusedBorderColor = HommWhite.copy(alpha = 0.6f),
            cursorColor = HommGold
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

fun calculateUpgrade(x: Int, y: Int, z: Int): String {
    val a = 2992.911117
    val b = 14174.264968
    val c = 5325.181015
    val d = 32788.72792

    val sum = a * x + b * y + c * z + d
    val bracketValue = floor(sum).toLong()

    val mod32768 = bracketValue % 32768
    val finalMod = mod32768 % 100

    return if (finalMod < 50) {
        "ЕСТЬ улучшенный стек"
    } else {
        "НЕТ улучшенного стека"
    }
}