package com.example.homm3reference.ui.utils

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.homm3reference.data.Creature
import com.example.homm3reference.data.GameData
import com.example.homm3reference.ui.common.AppBackground
import com.example.homm3reference.ui.common.ArmySlot
import com.example.homm3reference.ui.common.TownCard
import com.example.homm3reference.ui.creatures.CreatureCard
import com.example.homm3reference.ui.theme.HommGlassBackground
import com.example.homm3reference.ui.theme.HommGold
import com.example.homm3reference.ui.theme.HommWhite
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NecromancyCalculatorScreen() {
    // Состояние ввода
    var necromancyLevel by remember { mutableIntStateOf(0) } // 0: Базовый, 1: Продвинутый, 2: Эксперт
    var isSpecialist by remember { mutableStateOf(false) }
    var heroLevelStr by remember { mutableStateOf("1") }

    // Артефакты
    var hasAmulet by remember { mutableStateOf(false) }
    var hasCowl by remember { mutableStateOf(false) }
    var hasBoots by remember { mutableStateOf(false) }

    // Строения
    var amplifierCountStr by remember { mutableStateOf("0") }
    var hasGrail by remember { mutableStateOf(false) }

    // Существо
    var selectedCreature by remember { mutableStateOf<Creature?>(null) }
    var creatureCountStr by remember { mutableStateOf("1") }
    var showSelectionDialog by remember { mutableStateOf(false) }

    // --- ЛОГИКА РАСЧЕТА (HotA / FizMiG) ---

    // 1. Базовый процент навыка
    val necromancyBasePercent = when (necromancyLevel) {
        0 -> 5.0
        1 -> 10.0
        2 -> 15.0
        else -> 5.0
    }

    val heroLevel = heroLevelStr.toIntOrNull() ?: 1

    // 2. Бонус специалиста (5% за уровень к базе навыка)
    val skillPercent = if (isSpecialist) {
        necromancyBasePercent * (1.0 + 0.05 * heroLevel)
    } else {
        necromancyBasePercent
    }

    // 3. Остальные бонусы
    val amplifiers = amplifierCountStr.toIntOrNull() ?: 0
    val amplifierBonus = amplifiers * 5.0
    val grailBonus = if (hasGrail) 20.0 else 0.0

    val artifactBonus = (if (hasAmulet) 2.5 else 0.0) +
            (if (hasCowl) 5.0 else 0.0) +
            (if (hasBoots) 7.5 else 0.0)

    val totalPercent = min(100.0, skillPercent + amplifierBonus + grailBonus + artifactBonus)
    val percentFactor = totalPercent / 100.0

    // 4. Расчет количества
    val creatureCount = creatureCountStr.toIntOrNull() ?: 1
    val resultSkeletons: Int
    val resultWarriors: Int

    if (selectedCreature != null) {
        val totalHealth = selectedCreature!!.health.toDouble() * creatureCount
        val skeletonHealth = 6.0

        // ШАГ 1: Лимит по Очкам Некромантии (из здоровья)
        val necromancyPoints = totalHealth * percentFactor
        val limitByHP = floor(necromancyPoints / skeletonHealth).toInt()

        // ШАГ 2: Лимит по Количеству Существ (Правило HotA)
        val limitByUnitCount = floor(creatureCount * percentFactor).toInt()

        // ШАГ 3: Выбираем минимум
        var calculatedSkeletons = min(limitByHP, limitByUnitCount)

        // Минимальная гарантия 1
        if (calculatedSkeletons < 1 && totalPercent > 0 && creatureCount > 0) {
            calculatedSkeletons = 1
        }

        resultSkeletons = calculatedSkeletons

        // Скелеты-воины: 2/3 от обычных, округление вверх
        resultWarriors = ceil(resultSkeletons * 2.0 / 3.0).toInt()
    } else {
        resultSkeletons = 0
        resultWarriors = 0
    }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Заголовок с иконкой
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
            ) {
                Text(
                    text = "Калькулятор некромантии",
                    color = HommGold,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            // --- ЕДИНАЯ КАРТОЧКА ВВОДА ДАННЫХ ---
            HommCard {
                Column {
                    // 1. Уровень навыка
                    Text(
                        "Уровень Некромантии",
                        color = HommGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("Базовый", "Продв.", "Эксперт").forEachIndexed { index, label ->
                            FilterChip(
                                selected = necromancyLevel == index,
                                onClick = { necromancyLevel = index },
                                label = { Text(label, fontSize = 16.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = HommGold,
                                    selectedLabelColor = Color.Black,
                                    containerColor = Color.Transparent,
                                    labelColor = HommWhite
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = Color.Gray, // СЕРАЯ ОБВОДКА
                                    selectedBorderColor = HommGold,
                                    borderWidth = 1.dp, // 1.dp невыбранный
                                    selectedBorderWidth = 2.dp, // 2.dp выбранный
                                    enabled = true,
                                    selected = necromancyLevel == index
                                ),
                                modifier = Modifier.height(32.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. Артефакты и Специалист
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ArtifactToggle("util_vidomisra", isSpecialist) { isSpecialist = !isSpecialist }
                        ArtifactToggle("artifact_amulet_of_the_undertaker", hasAmulet) { hasAmulet = !hasAmulet }
                        ArtifactToggle("artifact_vampires_cowl", hasCowl) { hasCowl = !hasCowl }
                        ArtifactToggle("artifact_dead_mans_boots", hasBoots) { hasBoots = !hasBoots }
                    }

                    // Анимация появления поля уровня героя
                    AnimatedVisibility(
                        visible = isSpecialist,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(12.dp))
                            // Используем HommTextField из пакета ui.utils
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4. Строения
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically, // Центрируем по вертикали
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            HommTextField(
                                value = amplifierCountStr,
                                onValueChange = { str ->
                                    if (str.all { it.isDigit() }) {
                                        val num = str.toIntOrNull() ?: 0
                                        if (num <= 255) amplifierCountStr = str
                                    }
                                },
                                label = "УСИЛИТЕЛИ"
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        FilterChip(
                            selected = hasGrail,
                            onClick = { hasGrail = !hasGrail },
                            label = {
                                Text(
                                    "Грааль",
                                    fontSize = 16.sp,
                                    color = if (hasGrail) Color.Black else HommWhite
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = HommGold,
                                containerColor = Color.Transparent,
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = Color.Gray,
                                selectedBorderColor = HommGold,
                                borderWidth = 1.dp,
                                selectedBorderWidth = 2.dp,
                                enabled = true,
                                selected = hasGrail
                            ),
                            modifier = Modifier.height(60.dp) // Увеличили до 60.dp чтобы сравнять с TextField
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Итого
                    Text(
                        text = "Итого: ${String.format("%.2f", totalPercent)}%",
                        color = HommGold,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.End)
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = HommGold,
                        thickness = 2.dp
                    )

                    // 5. Вражеский отряд
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Кнопка выбора существа
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
                                value = creatureCountStr,
                                onValueChange = { if (it.all { c -> c.isDigit() }) creatureCountStr = it },
                                label = "КОЛ-ВО"
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
            Text("Воскрешенная армия", color = HommGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ArmySlot(imageRes = "creature_skeleton", count = "$resultSkeletons", onClick = {})
                    Text("Скелеты", color = HommWhite, fontSize = 16.sp, modifier = Modifier.padding(top = 4.dp), fontWeight = FontWeight.Bold)
                }
                Text("ИЛИ", color = HommGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ArmySlot(imageRes = "creature_skeleton_warrior", count = "$resultWarriors", onClick = {})
                    Text("Скелеты-воины", color = HommWhite, fontSize = 16.sp, modifier = Modifier.padding(top = 4.dp), fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        // Диалог
        if (showSelectionDialog) {
            CreaturePickerFlow(
                onDismiss = { showSelectionDialog = false },
                onCreatureSelected = { creature ->
                    selectedCreature = creature
                    showSelectionDialog = false
                }
            )
        }
    }
}

// --- Компоненты ---

@Composable
fun CreaturePickerFlow(
    onDismiss: () -> Unit,
    onCreatureSelected: (Creature) -> Unit
) {
    var selectedTown by remember { mutableStateOf<String?>(null) }
    val allCreatures = remember { GameData.creatures }
    val towns = remember(allCreatures) {
        val townOrder = listOf(
            "Замок", "Оплот", "Башня", "Инферно", "Некрополис", "Темница",
            "Цитадель", "Крепость", "Сопряжение", "Причал", "Фабрика",
            "Нейтралы", "Боевые машины"
        )
        allCreatures.map { it.town }.distinct().sortedBy { townOrder.indexOf(it) }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
            AppBackground {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { if (selectedTown != null) selectedTown = null else onDismiss() },
                            colors = ButtonDefaults.buttonColors(containerColor = HommGold),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            Text(text = if (selectedTown != null) "Назад" else "Закрыть", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = selectedTown ?: "Выберите фракцию", color = HommGold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(80.dp))
                    }
                    HorizontalDivider(color = HommGold)

                    if (selectedTown == null) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(towns) { town -> TownCard(townName = town, onClick = { selectedTown = town }) }
                        }
                    } else {
                        val townCreatures = remember(selectedTown) { allCreatures.filter { it.town == selectedTown } }
                        if (selectedTown == "Нейтралы" || selectedTown == "Боевые машины") {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(townCreatures) { creature -> CreatureCard(creature) { onCreatureSelected(it) } }
                            }
                        } else {
                            val levels = townCreatures.map { it.level }.distinct().sorted()
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(levels) { level ->
                                    val levelCreatures = townCreatures.filter { it.level == level }.sortedBy { it.isUpgraded }
                                    levelCreatures.chunked(2).forEach { rowCreatures ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            if (rowCreatures.isNotEmpty()) {
                                                Box(modifier = Modifier.weight(1f).padding(end = 8.dp)) { CreatureCard(rowCreatures[0]) { onCreatureSelected(it) } }
                                            } else {
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                            if (rowCreatures.size > 1) {
                                                Box(modifier = Modifier.weight(1f).padding(start = 8.dp)) { CreatureCard(rowCreatures[1]) { onCreatureSelected(it) } }
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
        }
    }
}

@Composable
fun HommCard(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(HommGlassBackground, RoundedCornerShape(8.dp))
            .border(2.dp, HommGold, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun ArtifactToggle(resIdName: String, isSelected: Boolean, onClick: () -> Unit) {
    val context = LocalContext.current
    val resId = getDrawableId(context, resIdName)

    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.6f))
            .border(
                width = if (isSelected) 2.dp else 1.dp, // 1.dp unselected like HommTextField
                color = if (isSelected) HommGold else HommWhite.copy(alpha = 0.6f), // Matching HommTextField unfocused
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

@SuppressLint("DiscouragedApi")
@Composable
fun getDrawableId(context: android.content.Context, name: String): Int {
    return context.resources.getIdentifier(name, "drawable", context.packageName)
}