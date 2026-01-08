package com.example.homm3reference.ui.magic

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homm3reference.data.Spell
import com.example.homm3reference.ui.common.AppBackground
import com.example.homm3reference.ui.common.AppSearchBar
import com.example.homm3reference.ui.common.HeroImage
import com.example.homm3reference.ui.theme.HommGlassBackground
import com.example.homm3reference.ui.theme.HommGold
import android.graphics.Color as AndroidColor
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState

// Локальные константы
private val HommShape = RoundedCornerShape(8.dp)
private val HommBorder = BorderStroke(2.dp, HommGold)

@Composable
fun MagicSchoolSelectScreen(
    onSchoolSelected: (String) -> Unit,
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    searchResultsContent: @Composable () -> Unit
) {
    AppBackground {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            Column(
                modifier = Modifier.padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Школы Магии",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = HommGold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                AppSearchBar(
                    query = searchQuery,
                    onQueryChanged = onQueryChanged,
                    placeholderText = "Поиск заклинания..."
                )
            }

            if (searchQuery.isNotBlank()) {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    searchResultsContent()
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp)) // Чуть больше отступ от поиска до карточек

                    // Первая строка
                    // ИСПРАВЛЕНИЕ: используем spacedBy(16.dp) для отступа МЕЖДУ элементами,
                    // а не padding внутри элементов.
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        MagicSchoolCard(
                            name = "Земля",
                            icon = "expert_earth_magic",
                            schoolId = "Earth",
                            onClick = onSchoolSelected,
                            modifier = Modifier.weight(1f) // Убрали padding(8.dp)
                        )
                        MagicSchoolCard(
                            name = "Вода",
                            icon = "expert_water_magic",
                            schoolId = "Water",
                            onClick = onSchoolSelected,
                            modifier = Modifier.weight(1f) // Убрали padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp)) // Отступ между рядами

                    // Вторая строка
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        MagicSchoolCard(
                            name = "Огонь",
                            icon = "expert_fire_magic",
                            schoolId = "Fire",
                            onClick = onSchoolSelected,
                            modifier = Modifier.weight(1f)
                        )
                        MagicSchoolCard(
                            name = "Воздух",
                            icon = "expert_air_magic",
                            schoolId = "Air",
                            onClick = onSchoolSelected,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MagicSchoolCard(
    name: String,
    icon: String,
    schoolId: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(180.dp), // Высокие карточки
        colors = CardDefaults.cardColors(containerColor = HommGlassBackground),
        border = HommBorder,
        shape = HommShape,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick(schoolId) }, // clickable перенесен внутрь, чтобы кликалась вся область Box
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                HeroImage(
                    imageName = icon,
                    width = 100.dp,
                    height = 100.dp,
                    contentScale = ContentScale.Fit,
                    borderWidth = -1.dp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = HommGold
                )
            }
        }
    }
}


@Composable
fun SpellCard(spell: Spell, onClick: () -> Unit) {
    // Вычисляем цвет: если в JSON есть backgroundColor, используем его, иначе стандартный HommGlassBackground
    val cardBackgroundColor = remember(spell.backgroundColor) {
        if (!spell.backgroundColor.isNullOrBlank()) {
            try {
                // Парсим hex-строку (например, "#006400") в Compose Color
                Color(AndroidColor.parseColor(spell.backgroundColor))
            } catch (e: Exception) {
                // Если цвет задан некорректно, используем стандартный
                HommGlassBackground
            }
        } else {
            HommGlassBackground
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        // Используем вычисленный цвет здесь
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        // border = HommBorder, // Можно раскомментировать, если нужна рамка
        shape = HommShape,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeroImage(imageName = spell.imageRes, width = 48.dp, height = 48.dp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = spell.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = HommGold
                )
                Text(
                    text = "Уровень: ${spell.level} | Мана: ${spell.manaCostNone}",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun SpellListScreen(
    school: String,
    spells: List<Spell>,
    listState: LazyListState = rememberLazyListState(),
    onSpellSelected: (Spell) -> Unit
) {

    val schoolName = when(school) {
        "Earth" -> "Магия Земли"
        "Water" -> "Магия Воды"
        "Fire" -> "Магия Огня"
        "Air" -> "Магия Воздуха"
        else -> "Заклинания"
    }
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    var searchQuery by remember { androidx.compose.runtime.mutableStateOf("") }

    val groupedSpells = remember(spells, searchQuery) {
        val filtered = if (searchQuery.isBlank()) spells else spells.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }
        filtered.groupBy { it.level }.toSortedMap().mapValues { entry ->
            entry.value.sortedBy { it.name }
        }
    }

    AppBackground {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = schoolName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = HommGold,
                modifier = Modifier.padding(vertical = 8.dp).align(Alignment.CenterHorizontally)
            )

            LazyColumn(
                // 2. Привязываем состояние
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = navBarPadding)
            ) {
                groupedSpells.forEach { (_, levelSpells) ->
                    items(levelSpells) { spell ->
                        SpellCard(spell = spell, onClick = { onSpellSelected(spell) })
                    }
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(thickness = 1.dp, color = HommGold.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SpellDetailScreen(spell: Spell) {
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                // ВАРИАНТ А: Паддинг для колонки
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp + navBarPadding // <-- Добавлен отступ снизу
                )
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // --- ШАПКА ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HeroImage(imageName = spell.imageRes, width = 80.dp, height = 80.dp, borderWidth = 2.dp)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = spell.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = HommGold
                    )
                    Text(
                        text = "${mapSchoolName(spell.school)}\n${spell.level} уровень",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = HommGold)

            // --- ТАБЛИЦА ---
            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                Text(text = "Навык", modifier = Modifier.weight(1f), color = HommGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "Мана", modifier = Modifier.weight(0.6f), color = HommGold, fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center)
                Text(text = "Эффект", modifier = Modifier.weight(2.5f), color = HommGold, fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center)
            }

            HorizontalDivider(color = Color.White, thickness = 1.dp)

            SpellEffectRow("Нет", spell.manaCostNone, spell.descriptionNone)
            HorizontalDivider(color = Color.White.copy(alpha = 0.3f))

            SpellEffectRow("Базовый", spell.manaCostBasic, spell.descriptionBasic)
            HorizontalDivider(color = Color.White.copy(alpha = 0.3f))

            SpellEffectRow("Продв.", spell.manaCostAdvanced, spell.descriptionAdvanced)
            HorizontalDivider(color = Color.White.copy(alpha = 0.3f))

            SpellEffectRow("Эксперт", spell.manaCostExpert, spell.descriptionExpert)
        }
    }
}

@Composable
fun SpellEffectRow(levelName: String, mana: Int, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = levelName, modifier = Modifier.weight(1f), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = mana.toString(), modifier = Modifier.weight(0.6f), color = Color(0xFF4FC3F7), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, fontSize = 16.sp)
        Text(text = description, modifier = Modifier.weight(2.5f), color = Color.White, fontSize = 16.sp, lineHeight = 20.sp)
    }
}

fun mapSchoolName(schoolId: String): String {
    return when(schoolId) {
        "Earth" -> "Магия Земли"
        "Air" -> "Магия Воздуха"
        "Fire" -> "Магия Огня"
        "Water" -> "Магия Воды"
        else -> "Все стихии"
    }
}