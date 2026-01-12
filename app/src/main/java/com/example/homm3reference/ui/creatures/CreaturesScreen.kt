package com.example.homm3reference.ui.creatures

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homm3reference.data.Creature
import com.example.homm3reference.ui.common.*
import com.example.homm3reference.ui.theme.HommGlassBackground
import com.example.homm3reference.ui.theme.HommGold
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import android.util.Log
import androidx.compose.runtime.LaunchedEffect

// Локальные константы
private val HommShape = RoundedCornerShape(8.dp)
private val HommBorder = BorderStroke(2.dp, HommGold)

@Composable
fun CreatureListScreen(
    townName: String,
    creatures: List<Creature>,
    listState: LazyListState = rememberLazyListState(),
    onCreatureSelected: (Creature) -> Unit
) {
    // --- НАЧАЛО ИЗМЕНЕНИЙ: Логирование статистики ---
    LaunchedEffect(townName, creatures) {
        val tag = "Homm3Creatures"

        Log.d(tag, "==========================================")
        Log.d(tag, "Экран города/группы: $townName")
        Log.d(tag, "Всего существ в списке: ${creatures.size}")
        Log.d(tag, "==========================================")

        Log.d(tag, "--- Разбивка по УРОВНЯМ (${creatures.groupBy { it.level }.size} групп) ---")
        creatures.groupBy { it.level }.toSortedMap().forEach { (level, items) ->
            Log.d(tag, "Уровень $level: ${items.size} шт.")
        }

        Log.d(tag, "\n--- Разбивка по УЛУЧШЕНИЯМ ---")
        val upgradedCount = creatures.count { it.isUpgraded }
        val baseCount = creatures.count { !it.isUpgraded }
        Log.d(tag, "Базовые: $baseCount")
        Log.d(tag, "Улучшенные: $upgradedCount")

        Log.d(tag, "==========================================")
    }
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    var searchQuery by remember { mutableStateOf("") }

    val filteredCreatures = remember(creatures, searchQuery) {
        if (searchQuery.isBlank()) creatures
        else creatures.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {

            Text(
                text = townName,
                modifier = Modifier
                    .padding(end = 16.dp, top = 48.dp)
                    .align(Alignment.CenterHorizontally),
                fontSize = 24.sp,
                color = HommGold,
                fontWeight = FontWeight.Bold
            )


            // Сетка для нейтралов/машин или Список для городов
            if (townName == "Нейтралы" || townName == "Боевые машины") {
                LazyVerticalGrid(
                    // Для сетки используем локальное состояние (или можно было прокинуть gridState)
                    state = rememberLazyGridState(),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp + navBarPadding
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredCreatures) { creature ->
                        CreatureCard(creature, onCreatureSelected)
                    }
                }
            } else {
                val levels = filteredCreatures.map { it.level }.distinct().sorted()

                LazyColumn(
                    // 2. Привязываем переданное состояние
                    state = listState,
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp + navBarPadding
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(levels) { level ->
                        // Сортировка: сначала обычные, потом грейженные
                        val levelCreatures = filteredCreatures.filter { it.level == level }.sortedBy { it.isUpgraded }

                        levelCreatures.chunked(2).forEach { rowCreatures ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                if (rowCreatures.isNotEmpty()) {
                                    Box(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                        CreatureCard(rowCreatures[0], onCreatureSelected)
                                    }
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }

                                if (rowCreatures.size > 1) {
                                    Box(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                                        CreatureCard(rowCreatures[1], onCreatureSelected)
                                    }
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }

                        if (level != levels.last()) {
                            HorizontalDivider(color = HommGold.copy(alpha = 0.5f), thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreatureCard(creature: Creature, onClick: (Creature) -> Unit) {
    val context = LocalContext.current
    val resId = remember(creature.imageRes) {
        context.resources.getIdentifier(creature.imageRes, "drawable", context.packageName)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp) // Немного увеличил высоту для комфорта
            .clickable { onClick(creature) },
        colors = CardDefaults.cardColors(containerColor = HommGlassBackground),
        border = HommBorder,
        shape = HommShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(120.dp), // Контейнер под картинку
                contentAlignment = Alignment.Center
            ) {
                if (resId != 0) {
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = creature.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = HommGold,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                maxLines = 2
            )
        }
    }
}

@Composable
fun CreatureDetailScreen(creature: Creature) {
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    AppBackground {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Шапка
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Картинка в рамке
                Box(
                    modifier = Modifier
                        .background(HommGlassBackground, HommShape)
                        .border(HommBorder, HommShape)
                        .padding(8.dp)
                ) {
                    HeroImage(
                        imageName = creature.imageRes,
                        width = 120.dp,
                        height = 150.dp,
                        contentScale = ContentScale.Fit,
                        borderWidth = -1.dp // Рамку рисуем выше
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = creature.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = HommGold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "${creature.town}", fontSize = 16.sp, color = Color.White)
                    Text(text = "Уровень ${creature.level}", fontSize = 16.sp, color = Color.White.copy(alpha = 0.9f))
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = HommGold)

            val damageString = if (creature.minDamage == creature.maxDamage) {
                "${creature.minDamage}"
            } else {
                "${creature.minDamage}-${creature.maxDamage}"
            }

            // Статы
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatItem("Атака", "⚔️", creature.attack.toString())
                StatItem("Защита", "🛡️", creature.defense.toString())
                StatItem("Урон", "💥", damageString)
                StatItem("ХП", "❤️", creature.health.toString())
                StatItem("Скор.", "🦶", creature.speed.toString())
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = HommGold)

            InfoRow("Цена", "${creature.goldCost} золота" + (if (creature.resourceCost != null) " + ${creature.resourceCost}" else ""))
            InfoRow("Прирост", "${creature.growth}")
            InfoRow("AI Value", "${creature.aiValue}")

            Spacer(modifier = Modifier.height(16.dp))
            Text("Способности:", color = HommGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = creature.abilities, color = Color.White, fontSize = 16.sp, lineHeight = 22.sp)

            Spacer(modifier = Modifier.height(16.dp + navBarPadding))
        }
    }
}