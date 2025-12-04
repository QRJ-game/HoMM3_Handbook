package com.example.homm3reference.ui.creatures

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homm3reference.data.Creature
import com.example.homm3reference.ui.common.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.remember
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.Image

@Composable
fun CreatureListScreen(
    townName: String,
    creatures: List<Creature>,
    onBack: () -> Unit,
    onCreatureSelected: (Creature) -> Unit
) {
    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Text("Назад", color = Color(0xFFD4AF37))
                }
            }
            Text(
                text = townName,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                fontSize = 24.sp,
                color = Color(0xFFD4AF37),
                fontWeight = FontWeight.Bold
            )

            if (townName == "Нейтралы" || townName == "Боевые машины") {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(creatures) { creature ->
                        CreatureCard(creature, onCreatureSelected)
                    }
                }
            } else {
                val levels = creatures.map { it.level }.distinct().sorted()
                LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(levels) { level ->
                        val levelCreatures = creatures.filter { it.level == level }.sortedBy { it.isUpgraded }

                        levelCreatures.chunked(2).forEach { rowCreatures ->
                            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
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
                            HorizontalDivider(color = Color.White, thickness = 1.dp, modifier = Modifier.padding(vertical = 2.dp))
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
            .height(170.dp)
            .clickable { onClick(creature) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                    .height(130.dp)
                    .clip(RoundedCornerShape(8.dp)) // Скругляем углы рамки
                    //.background(Color.Gray) // Фон, если картинка прозрачная или отсутствует
                    .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(8.dp)), // Золотая рамка
                contentAlignment = Alignment.BottomCenter // Выравниваем по низу, чтобы offset поднимал вверх
            ) {
                if (resId != 0) {
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(y = (-10).dp), // <--- СМЕЩЕНИЕ КАРТИНКИ ВВЕРХ НА 15dp
                        contentScale = ContentScale.Fit
                    )
                }
            }



            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = creature.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}


@Composable
fun CreatureDetailScreen(creature: Creature, onBack: () -> Unit) {
    AppBackground {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Text("Назад", color = Color(0xFFD4AF37))
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                // --- ИСПРАВЛЕНИЕ: Оборачиваем изображение в Card ---
                // Card автоматически применяет правильный цвет surface с учетом elevation (теней),
                // как это сделано в списках героев и существ.
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(4.dp) // Важно: такая же высота, как в списке
                ) {
                    HeroImage(
                        imageName = creature.imageRes,
                        width = 150.dp,
                        height = 195.dp,
                        contentScale = ContentScale.Fit,
                        borderWidth = 2.dp
                    )
                }
                // ---------------------------------------------------

                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = creature.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                    Text(text = "${creature.town} • Уровень ${creature.level}", fontSize = 16.sp, color = Color.White.copy(alpha = 0.9f))
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.White)

            val damageString = if (creature.minDamage == creature.maxDamage) {
                "${creature.minDamage}"
            } else {
                "${creature.minDamage}-${creature.maxDamage}"
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatItem("Атака", "⚔️", creature.attack.toString())
                StatItem("Защита", "🛡️", creature.defense.toString())
                StatItem("Урон", "💥", damageString)
                StatItem("Здоровье", "❤️", creature.health.toString())
                StatItem("Скорость", "🦶", creature.speed.toString())
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.White)
            InfoRow("Цена", "${creature.goldCost} золота" + (if (creature.resourceCost != null) " + ${creature.resourceCost}" else ""))
            InfoRow("Прирост", "${creature.growth}")
            InfoRow("AI Value", "${creature.aiValue}")

            Spacer(modifier = Modifier.height(16.dp))
            Text("Способности:", color = Color(0xFFD4AF37), fontWeight = FontWeight.Bold)
            Text(text = creature.abilities, color = Color.White)
        }
    }
}
