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
import com.example.homm3reference.ui.common.MenuButton

object ArtifactConstants {
    val MENU_ITEMS = listOf(
        "Класс" to "class",
        "Слот" to "slot",
        "Группа" to "group"
    )

    // Добавлен класс "set" для сборных артефактов
    val CLASSES = listOf("treasure", "minor", "major", "relic", "set")
    val SLOTS = listOf("Right Hand", "Left Hand", "Helmet", "Necklace", "Torso", "Feet", "Cape", "Ring", "Miscellaneous")
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
    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Артефакты", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                Spacer(modifier = Modifier.height(16.dp))
                AppSearchBar(query = searchQuery, onQueryChanged = onQueryChanged, placeholderText = "Поиск артефакта...")
            }

            if (searchQuery.isNotBlank()) {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) { searchResultsContent() }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ArtifactConstants.MENU_ITEMS.forEach { (label, type) ->
                        MenuButton(text = label, onClick = { onCategoryClick(type) })
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ArtifactCategorySelectScreen(categoryType: String, onValueClick: (String) -> Unit) {
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
            Text("Выберите категорию", fontSize = 22.sp, color = Color(0xFFD4AF37), modifier = Modifier.padding(bottom = 16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onValueClick(item) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(2.dp, Color(0xFF2D2D2D))
                    ) {
                        Text(text = item, modifier = Modifier.padding(16.dp), color = Color.White, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ArtifactListScreen(artifacts: List<Artifact>, onArtifactClick: (Artifact) -> Unit) {
    AppBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(artifacts) { artifact ->
                ArtifactCard(artifact = artifact, onClick = { onArtifactClick(artifact) })
            }
        }
    }
}

@Composable
fun ArtifactCard(artifact: Artifact, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D)),
        border = BorderStroke(2.dp, Color(0xFFD4AF37).copy(alpha = 0.5f))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            ArtifactImage(imageRes = artifact.imageRes, modifier = Modifier.size(64.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = artifact.name, color = Color(0xFFD4AF37), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = artifact.slot, color = Color.White, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun ArtifactDetailScreen(
    artifact: Artifact,
    onArtifactClick: (Artifact) -> Unit // Callback для навигации
) {
    // 1. Поиск родительского (Сборного) артефакта, если этот является частью
    val parentSetArtifact = remember(artifact) {
        if (!artifact.set.isNullOrEmpty() && artifact.set != "-") {
            // Ищем артефакт, у которого имя совпадает с полем 'set' текущего
            GameData.artifacts.find { it.name.equals(artifact.set, ignoreCase = true) && it.classType == "set" }
        } else {
            null
        }
    }

    // 2. Поиск составных частей, если это Сборный артефакт
    val childComponents = remember(artifact) {
        if (artifact.classType == "set") {
            GameData.artifacts.filter { it.set.equals(artifact.name, ignoreCase = true) }
        } else {
            emptyList()
        }
    }

    AppBackground {
        // Используем LazyColumn для всего экрана, чтобы список компонентов тоже скроллился
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- HEADER ---
            item {
                Spacer(modifier = Modifier.height(32.dp))
                ArtifactImage(imageRes = artifact.imageRes, modifier = Modifier.size(120.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = artifact.name,
                    color = Color(0xFFD4AF37),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // --- INFO CARD ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D).copy(alpha = 0.9f)),
                    border = BorderStroke(2.dp, Color(0xFFD4AF37))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        DetailRow("Класс", artifact.classType)
                        DetailRow("Слот", artifact.slot)
                        DetailRow("Группа", artifact.group)
                        DetailRow("Стоимость", "${artifact.goldCost}")

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color.White)
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = "Свойства:", color = Color(0xFFD4AF37), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = artifact.properties, color = Color.White, fontSize = 16.sp)

                        // --- ЛОГИКА ОТОБРАЖЕНИЯ СБОРНОГО АРТЕФАКТА ---
                        if (!artifact.set.isNullOrEmpty() && artifact.set != "-") {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Входит в набор:", color = Color(0xFFD4AF37))

                            if (parentSetArtifact != null) {
                                // Если нашли сборный артефакт в базе - делаем ссылку
                                Text(
                                    text = artifact.set,
                                    color = Color(0xFF4FC3F7), // Голубой цвет ссылки
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    textDecoration = TextDecoration.Underline,
                                    modifier = Modifier.clickable {
                                        onArtifactClick(parentSetArtifact)
                                    }
                                )
                            } else {
                                // Если не нашли (или ошибка данных) - просто текст
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
                        color = Color(0xFFD4AF37),
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

            // Отступ снизу для красоты
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(text = "$label:", color = Color(0xFFD4AF37), modifier = Modifier.padding(end = 16.dp))
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
            .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(8.dp))
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