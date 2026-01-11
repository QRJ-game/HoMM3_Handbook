package com.example.homm3reference.ui.artifacts

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homm3reference.data.Artifact
import com.example.homm3reference.data.GameData
import com.example.homm3reference.ui.common.AppBackground
import com.example.homm3reference.ui.common.AppSearchBar
import com.example.homm3reference.R
import com.example.homm3reference.ui.common.HommListCard
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import android.util.Log
import androidx.compose.runtime.LaunchedEffect

// Константы цветов для удобства и единообразия внутри файла
private val HommGold = Color(0xFFD4AF37)
private val HommGlassBackground = Color.Black.copy(alpha = 0.6f)
private val HommShape = RoundedCornerShape(8.dp)
private val HommBorder = BorderStroke(2.dp, HommGold)

object ArtifactConstants {
    val MENU_ITEMS = listOf(
        "Слот героя" to "slot",
        "Класс (редкость)" to "class",
        "Группа (свойство)" to "group"
    )

    val CLASSES = listOf("Сокровище", "Малый артефакт", "Великий артефакт", "Реликт", "Сборные артефакты")
    val SLOTS = listOf("Правая рука", "Левая рука", "Голова", "Шея", "Грудь", "Ноги", "Плечи", "Кольцо", "Разное")
    val GROUPS = listOf(
        "Оружие", "Щиты", "Головные уборы", "Доспехи",
        "Артефакты, влияющие на все характеристики героя",
        "Артефакты, влияющие на 2 характеристики героя",
        "Артефакты, влияющие на удачу и боевой дух",
        "Артефакты, влияющие на вторичные навыки героя",
        "Артефакты, влияющие на скорость и очки хода",
        "Артефакты, влияющие на магию героя",
        "Артефакты, влияющие на здоровье существ",
        "Кулоны, дающие защиту от определенных заклинаний",
        "Артефакты, приносящие ресурсы",
        "Артефакты, повышающие прирост существ",
        "Сборные артефакты",
        "Прочие артефакты"
    )
}

@Composable
fun ArtifactsMenuScreen(
    onCategoryClick: (String) -> Unit,
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    searchResultsContent: @Composable () -> Unit
) {
    // --- НАЧАЛО ИЗМЕНЕНИЙ: Логирование статистики ---
    LaunchedEffect(Unit) {
        val allArtifacts = GameData.artifacts
        val tag = "Homm3Artifacts"

        Log.d(tag, "==========================================")
        Log.d(tag, "Всего артефактов в базе: ${allArtifacts.size}")
        Log.d(tag, "==========================================")

        Log.d(tag, "--- Разбивка по СЛОТАМ (${allArtifacts.groupBy { it.slot }.size} групп) ---")
        allArtifacts.groupBy { it.slot }.forEach { (slot, items) ->
            Log.d(tag, "Слот '$slot': ${items.size}")
        }

        Log.d(tag, "\n--- Разбивка по КЛАССАМ (${allArtifacts.groupBy { it.classType }.size} групп) ---")
        allArtifacts.groupBy { it.classType }.forEach { (cls, items) ->
            Log.d(tag, "Класс '$cls': ${items.size}")
        }

        Log.d(tag, "\n--- Разбивка по ГРУППАМ (${allArtifacts.groupBy { it.group }.size} групп) ---")
        allArtifacts.groupBy { it.group }.forEach { (group, items) ->
            Log.d(tag, "Группа '$group': ${items.size}")
        }
        Log.d(tag, "==========================================")
    }
    // --- КОНЕЦ ИЗМЕНЕНИЙ ---

    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            // Заголовок и поиск
            Column(modifier = Modifier.padding(16.dp)) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Артефакты",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = HommGold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                AppSearchBar(
                    query = searchQuery,
                    onQueryChanged = onQueryChanged,
                    placeholderText = "Поиск артефакта..."
                )
            }

            if (searchQuery.isNotBlank()) {
                // --- ИЗМЕНЕНИЕ ЗДЕСЬ: Добавлен .weight(1f) ---
                Box(modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                ) {
                    searchResultsContent()
                }
            } else {
            Column(
                modifier = Modifier
                    .weight(1f) // Для меню тоже лучше добавить вес явно
                    .fillMaxWidth() // вместо fillMaxSize, так как мы внутри Column
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                    Text(
                        "Сортировка по:",
                        color = HommGold,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Используем LazyColumn для списка кнопок
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp, ),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 16.dp)
                    ) {
                        items(ArtifactConstants.MENU_ITEMS) { (label, type) ->
                            // Определяем иконку для каждой категории
                            val iconRes = when(type) {
                                "slot" -> "sort_slot"
                                "class" -> "sort_class"
                                "group" -> "sort_group"
                                else -> "icon_def" // Заглушка
                            }

                            HommListCard(
                                text = label,
                                imageRes = iconRes,
                                onClick = { onCategoryClick(type) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArtifactCategorySelectScreen(
    categoryType: String,
    // 1. Добавляем параметр состояния
    listState: LazyListState = rememberLazyListState(),
    onValueClick: (String) -> Unit
) {
    val items = remember(categoryType) {
        when (categoryType) {
            "class" -> ArtifactConstants.CLASSES
            "slot" -> ArtifactConstants.SLOTS
            "group" -> ArtifactConstants.GROUPS
            else -> emptyList()
        }
    }

    AppBackground {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "Выберите категорию",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = HommGold,
                modifier = Modifier
                    .padding(bottom = 16.dp, top = 32.dp)
                    .align(Alignment.CenterHorizontally)
            )
            LazyColumn(
                // 2. Привязываем состояние
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 16.dp)
            ) {
                items(items) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onValueClick(item) },
                        colors = CardDefaults.cardColors(containerColor = HommGlassBackground),
                        // border = HommBorder, // Убрали внешнюю рамку у карточки
                        elevation = CardDefaults.cardElevation(4.dp), // Добавили тень как у артефактов
                        shape = HommShape
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Иконка в стиле ArtifactImage
                            Box(
                                modifier = Modifier
                                    .size(64.dp) // Размер как у артефактов (было 48)
                                    .background(Color.Black.copy(alpha = 0.6f), HommShape)
                                    .border(2.dp, HommGold, HommShape) // Внутренняя золотая рамка картинки
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = getCategoryIcon(item)),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = item,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArtifactListScreen(
    artifacts: List<Artifact>,
    // 1. Добавляем параметр состояния
    listState: LazyListState = rememberLazyListState(),
    onArtifactClick: (Artifact) -> Unit
) {
    AppBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            // 2. Привязываем состояние к списку
            state = listState,
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        )
        {
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            items(artifacts) { artifact ->
                ArtifactCard(artifact = artifact, onClick = { onArtifactClick(artifact) })
            }
        }
    }
}

@Composable
fun ArtifactCard(artifact: Artifact, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = HommGlassBackground),
        //border = HommBorder,
        shape = HommShape
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ArtifactImage(imageRes = artifact.imageRes, modifier = Modifier.size(64.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = artifact.name,
                    color = HommGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = artifact.slot,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun ArtifactDetailScreen(
    artifact: Artifact,
    onArtifactClick: (Artifact) -> Unit
) {
    val parentSetArtifact = remember(artifact) {
        if (!artifact.set.isNullOrEmpty() && artifact.set != "-") {
            GameData.artifacts.find { it.name.equals(artifact.set, ignoreCase = true) && it.classType == "Сборные артефакты" }
        } else {
            null
        }
    }

    val childComponents = remember(artifact) {
        if (artifact.classType == "Сборные артефакты") {
            GameData.artifacts.filter { it.set.equals(artifact.name, ignoreCase = true) }
        } else {
            emptyList()
        }
    }

    AppBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            // --- ИЗМЕНЕНИЕ ЗДЕСЬ ---
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 16.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
                ArtifactImage(imageRes = artifact.imageRes, modifier = Modifier.size(120.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = artifact.name,
                    color = HommGold,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // --- INFO CARD ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = HommGlassBackground),
                    border = HommBorder,
                    shape = HommShape
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        DetailRow("Класс", artifact.classType)
                        DetailRow("Слот", artifact.slot)
                        DetailRow("Группа", artifact.group)
                        DetailRow("Стоимость", "${artifact.goldCost}")

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = HommGold, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = "Свойства:", color = HommGold, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = artifact.properties, color = Color.White, fontSize = 16.sp, lineHeight = 22.sp)

                        // --- ЛОГИКА ОТОБРАЖЕНИЯ СБОРНОГО АРТЕФАКТА ---
                        if (!artifact.set.isNullOrEmpty() && artifact.set != "-") {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Входит в набор:", color = HommGold, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(4.dp))

                            if (parentSetArtifact != null) {
                                Text(
                                    text = artifact.set,
                                    color = Color(0xFF4FC3F7), // Light Blue link
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    textDecoration = TextDecoration.Underline,
                                    modifier = Modifier.clickable {
                                        onArtifactClick(parentSetArtifact)
                                    }
                                )
                            } else {
                                Text(text = artifact.set, color = Color.Magenta, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // --- СПИСОК КОМПОНЕНТОВ (Для сборных артефактов) ---
            if (childComponents.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Состоит из:",
                        color = HommGold,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(childComponents) { component ->
                    ArtifactCard(artifact = component) {
                        onArtifactClick(component)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$label:",
            color = HommGold,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 16.dp)
        )
        Text(
            text = value,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun ArtifactImage(imageRes: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageId = remember(imageRes) {
        context.resources.getIdentifier(imageRes, "drawable", context.packageName)
    }

    Box(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.6f), HommShape)
            .border(2.dp, HommGold, HommShape) // Золотая рамка для всех картинок
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        if (imageId != 0) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        } else {
            Text("?", color = Color.White, fontSize = 24.sp)
        }
    }
}

private fun getCategoryIcon(categoryName: String): Int {
    return when (categoryName) {
        // --- CLASSES (Классы) ---
        "Сокровище" -> R.drawable.artifact_stoic_watchman
        "Малый артефакт" -> R.drawable.artifact_greater_gnolls_flail
        "Великий артефакт" -> R.drawable.artifact_cape_of_velocity
        "Реликт" -> R.drawable.artifact_helm_of_heavenly_enlightenment
        "Сборные артефакты" -> R.drawable.artifact_angelic_alliance

        // --- SLOTS (Слоты) ---
        "Правая рука" -> R.drawable.artifact_sword_of_judgement
        "Левая рука" -> R.drawable.artifact_lions_shield_of_courage
        "Голова" -> R.drawable.artifact_skull_helmet
        "Шея" -> R.drawable.artifact_necklace_of_dragonteeth
        "Грудь" -> R.drawable.artifact_titans_cuirass
        "Ноги" -> R.drawable.artifact_boots_of_speed
        "Плечи" -> R.drawable.artifact_cape_of_velocity
        "Кольцо" -> R.drawable.artifact_ring_of_vitality
        "Разное" -> R.drawable.artifact_inexhaustible_cart_of_lumber

        // --- GROUPS (Группы) ---
        "Оружие" -> R.drawable.artifact_blackshard_of_the_dead_knight
        "Щиты" -> R.drawable.artifact_shield_of_the_damned
        "Головные уборы" -> R.drawable.artifact_crown_of_the_supreme_magi
        "Доспехи" -> R.drawable.artifact_breastplate_of_petrified_wood
        "Артефакты, влияющие на все характеристики героя" -> R.drawable.artifact_sandals_of_the_saint
        "Артефакты, влияющие на 2 характеристики героя" -> R.drawable.artifact_red_dragon_flame_tongue
        "Артефакты, влияющие на удачу и боевой дух" -> R.drawable.artifact_clover_of_fortune
        "Артефакты, влияющие на вторичные навыки героя" -> R.drawable.artifact_dead_mans_boots
        "Артефакты, влияющие на скорость и очки хода" -> R.drawable.artifact_boots_of_speed
        "Артефакты, влияющие на магию героя" -> R.drawable.artifact_tome_of_earth_magic
        "Артефакты, влияющие на здоровье существ" -> R.drawable.artifact_vial_of_lifeblood
        "Кулоны, дающие защиту от определенных заклинаний" -> R.drawable.artifact_pendant_of_negativity
        "Артефакты, приносящие ресурсы" -> R.drawable.artifact_everpouring_vial_of_mercury
        "Артефакты, повышающие прирост существ" -> R.drawable.artifact_legs_of_legion
        "Прочие артефакты" -> R.drawable.artifact_shackles_of_war

        else -> R.drawable.artifact_grail // Заглушка
    }
}