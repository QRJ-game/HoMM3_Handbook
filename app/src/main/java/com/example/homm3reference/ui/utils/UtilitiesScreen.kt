package com.example.homm3reference.ui.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homm3reference.ui.common.AppBackground
import com.example.homm3reference.ui.common.HommListCard
import com.example.homm3reference.ui.theme.HommGold

@Composable
fun UtilitiesMenuScreen(
    onUpgradeCheckerClick: () -> Unit,
    onNecromancyClick: () -> Unit,
    onDemonRaisingClick: () -> Unit // <--- ВОТ ЭТОГО ПАРАМЕТРА НЕ ХВАТАЛО
) {
    AppBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Утилиты",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = HommGold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Кнопка 1: Checker
                        HommListCard(
                            text = "Проверка на улучшенный отряд",
                            imageRes = "util_checker",
                            onClick = onUpgradeCheckerClick
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // Кнопка 2: Necromancy
                        HommListCard(
                            text = "Калькулятор некромантии",
                            imageRes = "util_necro",
                            onClick = onNecromancyClick
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // Кнопка 3: Demon Raising
                        HommListCard(
                            text = "Поднятие демонов",
                            imageRes = "creature_pit_lord",
                            onClick = onDemonRaisingClick
                        )
                    }
                }
            }
        }
    }
}