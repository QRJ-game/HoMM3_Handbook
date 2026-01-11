package com.example.homm3reference.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Добавлены импорты для отступов навигационной панели
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import com.example.homm3reference.data.GameData

@Composable
fun TownSelectionScreen(
    title: String,
    towns: List<String>,
    onTownSelected: (String) -> Unit,
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    gridState: LazyGridState = rememberLazyGridState(),
    searchResultsContent: @Composable () -> Unit
) {
    // Вычисляем отступ снизу для навигационной панели
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    LaunchedEffect(Unit) {
        val allCreatures = GameData.creatures
        val tag = "Homm3CreaturesGlobal"

        Log.d(tag, "==========================================")
        Log.d(tag, "ГЛОБАЛЬНАЯ СТАТИСТИКА (Все существа)")
        Log.d(tag, "Всего существ в базе: ${allCreatures.size}")
        Log.d(tag, "==========================================")

        Log.d(tag, "--- Разбивка по ГОРОДАМ (${allCreatures.groupBy { it.town }.size} фракций) ---")
        allCreatures.groupBy { it.town }.forEach { (town, list) ->
            Log.d(tag, "Фракция '$town': ${list.size} существ")
        }

        Log.d(tag, "\n--- Разбивка по УРОВНЯМ ---")
        allCreatures.groupBy { it.level }.toSortedMap().forEach { (lvl, list) ->
            Log.d(tag, "Уровень $lvl: ${list.size} шт.")
        }

        val upgraded = allCreatures.count { it.isUpgraded }
        val base = allCreatures.count { !it.isUpgraded }
        Log.d(tag, "\n--- По улучшениям ---")
        Log.d(tag, "Базовые: $base")
        Log.d(tag, "Улучшенные: $upgraded")

        Log.d(tag, "==========================================")

        val allHeroes = GameData.heroes
        val tagH = "Homm3HeroesGlobal"

        Log.d(tagH, "==========================================")
        Log.d(tagH, "ГЛОБАЛЬНАЯ СТАТИСТИКА (Все Герои)")
        Log.d(tagH, "Всего героев в базе: ${allHeroes.size}")
        Log.d(tagH, "==========================================")

        Log.d(tagH, "--- Разбивка по ГОРОДАМ (${allHeroes.groupBy { it.town }.size}) ---")
        allHeroes.groupBy { it.town }.forEach { (town, list) ->
            Log.d(tagH, "$town: ${list.size}")
        }

        Log.d(tagH, "\n--- Разбивка по КЛАССАМ (${allHeroes.groupBy { it.heroClass }.size}) ---")
        allHeroes.groupBy { it.heroClass }.forEach { (cls, list) ->
            Log.d(tagH, "$cls: ${list.size}")
        }

        val special = allHeroes.count { !it.backgroundColor.isNullOrEmpty() }
        Log.d(tagH, "\nСпециальные (цветные): $special")
        Log.d(tagH, "Обычные: ${allHeroes.size - special}")
        Log.d(tagH, "==========================================")
    }
    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {

            // Заголовок и поиск
            Column(modifier = Modifier.padding(16.dp)) {
                // Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD4AF37),
                    modifier = Modifier
                        .padding(top = 32.dp, bottom = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                //Spacer(modifier = Modifier.height(16.dp))

                AppSearchBar(
                    query = searchQuery,
                    onQueryChanged = onQueryChanged,
                    placeholderText = "Поиск..."
                )
            }

            if (searchQuery.isNotBlank()) {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    searchResultsContent()
                }
            } else {
                LazyVerticalGrid(
                    // ВАЖНО: Привязываем состояние
                    state = gridState,
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = navBarPadding + 8.dp
                    )
                ) {
                    items(towns) { town ->
                        TownCard(townName = town, onClick = { onTownSelected(town) })
                    }
                }
            }
        }
    }
}