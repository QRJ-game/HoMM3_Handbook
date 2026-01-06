package com.example.homm3reference.ui.utils

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homm3reference.data.Creature
import com.example.homm3reference.data.GameData
import com.example.homm3reference.ui.common.AppBackground
import com.example.homm3reference.ui.common.ArmySlot
import com.example.homm3reference.ui.common.TownSelectionScreen
import com.example.homm3reference.ui.creatures.CreatureCard
import com.example.homm3reference.ui.creatures.CreatureListScreen
import com.example.homm3reference.ui.theme.HommGlassBackground
import com.example.homm3reference.ui.theme.HommGold
import com.example.homm3reference.ui.theme.HommWhite
import kotlin.math.floor
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemonRaisingCalculatorScreen() {
    // --- Состояние ---

    // Властители
    var pitLordCountStr by remember { mutableStateOf("1") }

    // Первая помощь
    var firstAidLevel by remember { mutableIntStateOf(0) } // 0: Нет, 1: Баз, 2: Прод, 3: Эксп
    var isSpecialist by remember { mutableStateOf(false) }
    var heroLevelStr by remember { mutableStateOf("1") }

    // Артефакты
    var hasRingVitality by remember { mutableStateOf(false) } // +1
    var hasRingLife by remember { mutableStateOf(false) }     // +1
    var hasVialLifeblood by remember { mutableStateOf(false) } // +2
    var hasElixir by remember { mutableStateOf(false) }       // Сет: +4 и +25% HP

    // Жертва
    var selectedCreature by remember { mutableStateOf<Creature?>(null) }
    var deadCountStr by remember { mutableStateOf("1") }
    var showSelectionDialog by remember { mutableStateOf(false) }

    // --- ЛОГИКА РАСЧЕТА ---

    val pitLords = pitLordCountStr.toIntOrNull() ?: 0
    val deadCount = deadCountStr.toIntOrNull() ?: 1
    val resultDemons: Int
    val calculationError: String?

    // Для отображения финального HP в UI
    var finalHpDisplay = 0

    if (selectedCreature != null) {
        val c = selectedCreature!!

        // Проверка на запрещенные типы
        if (c.isUndead) {
            calculationError = "Нельзя поднять из Нежити"
            resultDemons = 0
        } else if (c.isElemental) {
            calculationError = "Нельзя поднять из Элементалей"
            resultDemons = 0
        } else if (c.isGolem) {
            calculationError = "Нельзя поднять из Големов/Гаргулий"
            resultDemons = 0
        } else if (c.isWarMachine) {
            calculationError = "Нельзя поднять из Боевых машин"
            resultDemons = 0
        } else if (c.isMech) {
            calculationError = "Нельзя поднять из Механизмов"
            resultDemons = 0
        } else {
            calculationError = null

            val baseHp = c.health.toDouble()

            // 1. Бонус Палатки (расчет от базового, округление вниз)
            val tentBasePercent = when(firstAidLevel) {
                1 -> 5.0
                2 -> 10.0
                3 -> 15.0
                else -> 0.0
            }

            // Специалист: k * (1 + N * 0.05)
            val heroLevel = heroLevelStr.toIntOrNull() ?: 1
            val tentFinalPercent = if (isSpecialist) {
                tentBasePercent * (1.0 + 0.05 * heroLevel)
            } else {
                tentBasePercent
            }

            val tentHpBonus = floor(baseHp * (tentFinalPercent / 100.0)).toInt()

            // 2. Бонус Эликсира Жизни (+25% от базового, округление вниз)
            val elixirPercentBonus = if (hasElixir) {
                floor(baseHp * 0.25).toInt()
            } else {
                0
            }

            // 3. Плоские бонусы артефактов
            val flatArtifactBonus = if (hasElixir) {
                4
            } else {
                (if (hasRingVitality) 1 else 0) +
                        (if (hasRingLife) 1 else 0) +
                        (if (hasVialLifeblood) 2 else 0)
            }

            // Итоговое ХП одного трупа
            val hpPerUnit = baseHp.toInt() + tentHpBonus + elixirPercentBonus + flatArtifactBonus
            finalHpDisplay = hpPerUnit

            // АЛГОРИТМ РАСЧЕТА

            // А. Общий пул здоровья мертвых существ
            val totalDeadHealth = hpPerUnit * deadCount

            // Б. Сила воскрешения Властителей (50 HP за каждого Властителя)
            val raisingPower = pitLords * 50

            // В. Доступное для воскрешения здоровье
            val availableHealth = min(totalDeadHealth, raisingPower)

            // Г. Количество демонов (35 HP за демона)
            val demonsByHp = floor(availableHealth.toDouble() / 35.0).toInt()

            // Д. Ограничение по количеству трупов
            resultDemons = min(demonsByHp, deadCount)
        }
    } else {
        resultDemons = 0
        calculationError = null
    }

    // --- UI ---
    AppBackground {
        Box(modifier = Modifier.fillMaxSize()) {

            // Ваш основной контент
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Заголовок
                Text(
                    text = "Поднятие демонов",
                    color = HommGold,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                )

                // --- ЕДИНАЯ КАРТОЧКА ВВОДА ДАННЫХ ---
                HommCard {
                    Column {
                        // 1. Властители
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val pitIcon = getDrawableId(LocalContext.current, "creature_pit_lord")
                            if(pitIcon != 0) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .border(1.dp, HommGold, RoundedCornerShape(8.dp))
                                        .clip(RoundedCornerShape(8.dp))
                                ) {
                                    Image(
                                        painter = painterResource(id = pitIcon),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .matchParentSize()
                                            .offset(y = (-5).dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                HommTextField(
                                    value = pitLordCountStr,
                                    onValueChange = { if (it.all { c -> c.isDigit() }) pitLordCountStr = it },
                                    label = "КОЛ-ВО ВЛАСТИТЕЛЕЙ"
                                )
                            }
                        }

                        HorizontalDivider(color = HommGold, modifier = Modifier.padding(vertical = 12.dp))

                        // 2. Артефакты и Специалист
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Логика: если специалист выбран, а навык "Нет", ставим "Базовый"
                            SmallArtifactToggle("util_palatka", isSpecialist) {
                                isSpecialist = !isSpecialist
                                if (isSpecialist && firstAidLevel == 0) {
                                    firstAidLevel = 1
                                }
                            }

                            SmallArtifactToggle("artifact_ring_of_vitality", hasRingVitality) {
                                if(!hasElixir) hasRingVitality = !hasRingVitality
                            }
                            SmallArtifactToggle("artifact_ring_of_life", hasRingLife) {
                                if(!hasElixir) hasRingLife = !hasRingLife
                            }
                            SmallArtifactToggle("artifact_vial_of_lifeblood", hasVialLifeblood) {
                                if(!hasElixir) hasVialLifeblood = !hasVialLifeblood
                            }

                            SmallArtifactToggle("artifact_elixir_of_life", hasElixir) {
                                hasElixir = !hasElixir
                                if (hasElixir) {
                                    hasRingVitality = true
                                    hasRingLife = true
                                    hasVialLifeblood = true
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = isSpecialist,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column {
                                Spacer(modifier = Modifier.height(12.dp))
                                HommTextField(
                                    value = heroLevelStr,
                                    onValueChange = { str ->
                                        if (str.all { it.isDigit() }) {
                                            val num = str.toIntOrNull() ?: 0
                                            if (num <= 74) heroLevelStr = str
                                        }
                                    },
                                    label = "УРОВЕНЬ ГЕРОЯ"
                                )
                            }
                        }

                        // Убрали разделитель, добавили заголовок
                        Text(
                            text = "Навык Первой помощи",
                            color = HommGold,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        )

                        // 3. Навык Первая Помощь (кнопки)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val levels = listOf("Нет", "Базовый", "Продв.", "Эксперт")
                            levels.forEachIndexed { index, label ->
                                FilterChip(
                                    selected = firstAidLevel == index,
                                    onClick = { firstAidLevel = index },
                                    label = { Text(label, fontSize = 16.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = HommGold,
                                        selectedLabelColor = Color.Black,
                                        containerColor = Color.Transparent,
                                        labelColor = HommWhite
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        borderColor = Color.Gray,
                                        selectedBorderColor = HommGold,
                                        borderWidth = 1.dp,
                                        selectedBorderWidth = 2.dp,
                                        enabled = true,
                                        selected = firstAidLevel == index
                                    ),
                                    modifier = Modifier.height(32.dp)
                                )
                            }
                        }

                        HorizontalDivider(color = HommGold, modifier = Modifier.padding(vertical = 12.dp))

                        // 4. Погибший отряд
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(HommGlassBackground)
                                    .border(2.dp, HommGold, RoundedCornerShape(8.dp))
                                    .clickable { showSelectionDialog = true },
                                contentAlignment = Alignment.Center
                            ) {
                                if (selectedCreature != null) {
                                    val creatureResId = getDrawableId(LocalContext.current, selectedCreature!!.imageRes)
                                    if (creatureResId != 0) {
                                        Image(
                                            painter = painterResource(id = creatureResId),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize().padding(4.dp),
                                            contentScale = ContentScale.Fit
                                        )
                                    } else {
                                        Text("?", color = Color.Red, fontSize = 12.sp)
                                    }
                                } else {
                                    Text("?", fontSize = 40.sp, color = HommGold, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.Center
                            ) {
                                HommTextField(
                                    value = deadCountStr,
                                    onValueChange = { if (it.all { c -> c.isDigit() }) deadCountStr = it },
                                    label = "КОЛ-ВО ПОГИБШИХ"
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                if (selectedCreature != null) {
                                    Text(
                                        text = selectedCreature!!.name,
                                        color = HommGold,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                    Text(
                                        text = "HP (итог): $finalHpDisplay",
                                        color = HommWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                } else {
                                    Text(
                                        text = "ВЫБЕРИТЕ ВРАГА",
                                        color = Color.Gray,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- Результат ---
                if (calculationError != null) {
                    Text(
                        text = calculationError,
                        color = Color.Red,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text("Поднятые Демоны", color = HommGold, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        ArmySlot(imageRes = "creature_demon", count = "$resultDemons", onClick = {})
                        Text(
                            text = "Демоны",
                            color = HommWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            } // End of Column

            if (showSelectionDialog) {
                UnifiedCreaturePickerDialog(
                    onDismiss = { showSelectionDialog = false },
                    onCreatureSelected = {
                        selectedCreature = it
                        showSelectionDialog = false
                    }
                )
            }
        } // End of Box
    } // End of AppBackground
}

// Переименовали, чтобы не конфликтовать с ArtifactToggle в другом файле
@Composable
private fun SmallArtifactToggle(resIdName: String, isSelected: Boolean, onClick: () -> Unit) {
    val context = LocalContext.current
    val resId = getDrawableId(context, resIdName)

    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.6f))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) HommGold else HommWhite.copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        if (resId != 0) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

// НОВАЯ ФУНКЦИЯ: Объединяет экраны выбора города и существа
// БЕЗ КНОПОК НАВИГАЦИИ, ТОЛЬКО СИСТЕМНЫЙ BACK
@Composable
fun UnifiedCreaturePickerDialog(
    onDismiss: () -> Unit,
    onCreatureSelected: (Creature) -> Unit
) {
    // Состояние внутри
    var selectedTown by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val allCreatures = remember { GameData.creatures }
    val towns = remember(allCreatures) {
        val townOrder = listOf(
            "Замок", "Оплот", "Башня", "Инферно", "Некрополис", "Темница",
            "Цитадель", "Крепость", "Сопряжение", "Причал", "Фабрика", "Кронверк",
            "Нейтралы", "Боевые машины"
        )
        allCreatures.map { it.town }.distinct().sortedBy { townOrder.indexOf(it) }
    }

    // Обработка кнопки НАЗАД
    BackHandler {
        if (selectedTown != null) {
            // Если выбран город, сбрасываем его (возврат к списку городов)
            selectedTown = null
        } else {
            // Если города нет (список городов), закрываем диалог
            onDismiss()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Подложка
    ) {
        if (selectedTown == null) {
            // ЭКРАН ВЫБОРА ГОРОДА
            TownSelectionScreen(
                title = "Выберите фракцию",
                towns = towns,
                onTownSelected = { selectedTown = it },
                searchQuery = searchQuery,
                onQueryChanged = { searchQuery = it },
                searchResultsContent = {
                    val filtered = allCreatures.filter {
                        it.name.contains(searchQuery, ignoreCase = true)
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filtered) { creature ->
                            CreatureCard(creature) { onCreatureSelected(it) }
                        }
                    }
                }
            )
            // КНОПКА "ЗАКРЫТЬ" УБРАНА

        } else {
            // ЭКРАН ВЫБОРА СУЩЕСТВА
            Box(modifier = Modifier.fillMaxSize()) {
                CreatureListScreen(
                    townName = selectedTown!!,
                    creatures = allCreatures.filter { it.town == selectedTown },
                    onCreatureSelected = onCreatureSelected
                )
                // КНОПКА "НАЗАД" УБРАНА
            }
        }
    }
}