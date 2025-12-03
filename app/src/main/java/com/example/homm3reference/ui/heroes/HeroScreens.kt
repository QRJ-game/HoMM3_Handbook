package com.example.homm3reference.ui.heroes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt // <-- Импорт для KTX Color
import com.example.homm3reference.data.Creature
import com.example.homm3reference.data.Hero
import com.example.homm3reference.data.HeroBaseStats
import com.example.homm3reference.ui.common.*

@Composable
fun ClassSelectionScreen(
    townName: String,
    mightClassName: String,
    magicClassName: String,
    onBack: () -> Unit,
    onClassSelected: (String) -> Unit
) {
    AppBackground {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
            ) { Text("Назад", color = Color(0xFFD4AF37)) }

            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "$townName: Классы",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD4AF37)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Card(
                    modifier = Modifier.weight(1f).height(150.dp).padding(8.dp)
                        .clickable { onClassSelected("Might") },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4A3B3B))
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⚔️", fontSize = 40.sp)
                            Text(mightClassName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Воин", fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                }

                Card(
                    modifier = Modifier.weight(1f).height(150.dp).padding(8.dp)
                        .clickable { onClassSelected("Magic") },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF3B3B4A))
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⚡", fontSize = 40.sp)
                            Text(magicClassName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Маг", fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeroListScreen(
    heroes: List<Hero>,
    townName: String,
    className: String,
    onBack: () -> Unit,
    onHeroSelected: (Hero) -> Unit
) {
    val groupedHeroes = remember(heroes) {
        val standardHeroes = heroes.filter { it.backgroundColor.isNullOrEmpty() }
        val coloredHeroes = heroes.filter { !it.backgroundColor.isNullOrEmpty() }.groupBy { it.backgroundColor }
        Pair(standardHeroes, coloredHeroes)
    }

    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                ) { Text("Назад", color = Color(0xFFD4AF37)) }
            }
            Text(
                text = "$townName • $className",
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                fontSize = 20.sp,
                color = Color(0xFFD4AF37),
                fontWeight = FontWeight.Bold
            )
            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(groupedHeroes.first) { hero ->
                    HeroCard(hero, onHeroSelected)
                }
                if (groupedHeroes.first.isNotEmpty() && groupedHeroes.second.isNotEmpty()) {
                    item { HorizontalDivider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp)) }
                }
                groupedHeroes.second.entries.forEachIndexed { index, entry ->
                    if (index > 0) {
                        item { HorizontalDivider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp)) }
                    }
                    items(entry.value) { hero ->
                        HeroCard(hero, onHeroSelected)
                    }
                }
            }
        }
    }
}

@Composable
fun HeroCard(hero: Hero, onHeroSelected: (Hero) -> Unit) {
    // ИСПРАВЛЕНО: использование toColorInt() и переименование 'e' в '_'
    val cardColor = if (!hero.backgroundColor.isNullOrEmpty()) {
        try {
            Color(hero.backgroundColor.toColorInt())
        } catch (_: Exception) {
            MaterialTheme.colorScheme.surface
        }
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onHeroSelected(hero) },
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            HeroImage(imageName = hero.imageRes, width = 64.dp, height = 64.dp)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = hero.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun HeroDetailScreen(hero: Hero, creatures: List<Creature>, onBack: () -> Unit) {
    val stats = remember(hero.heroClass) { HeroBaseStats.getStats(hero.heroClass) }
    var selectedCreature by remember { mutableStateOf<Creature?>(null) }

    AppBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
            ) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                ) { Text("Назад", color = Color(0xFFD4AF37)) }
                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    HeroImage(
                        imageName = hero.imageRes,
                        width = 100.dp,
                        height = 100.dp,
                        borderWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        OutlinedText(
                            text = hero.name,
                            fontSize = 32.sp,
                            strokeWidth = 6f
                        )
                        Text(text = hero.town, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = hero.heroClass, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.White.copy(alpha = 0.8f))
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.Gray)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatItem("Атака", "⚔️", stats.attack.toString())
                    StatItem("Защита", "🛡️", stats.defense.toString())
                    StatItem("Сила", "⚡", stats.power.toString())
                    StatItem("Знания", "📖", stats.knowledge.toString())
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.Gray)

                InfoRow("Специализация", hero.specialty)
                InfoRow("Навыки", hero.skills)
                InfoRow("Заклинание", hero.spell ?: "Нет")

                Spacer(modifier = Modifier.height(16.dp))
                Text("Стартовая армия:", color = Color(0xFFD4AF37), fontWeight = FontWeight.Black, fontSize = 18.sp)

                ArmyVisuals(armyString = hero.army, onCreatureClick = { clickedImageRes ->
                    selectedCreature = creatures.find { it.imageRes == clickedImageRes }
                })
            }

            if (selectedCreature != null) {
                CreaturePopup(
                    creature = selectedCreature!!,
                    onDismiss = { selectedCreature = null }
                )
            }
        }
    }
}

@Composable
fun CreaturePopup(creature: Creature, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clickable(enabled = false) {},
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeroImage(
                    imageName = creature.imageRes,
                    width = 100.dp,
                    height = 130.dp,
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(creature.name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                Text("Уровень ${creature.level}", fontSize = 16.sp, color = Color.Gray)

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.Gray)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    StatItem("Атака", "⚔️", creature.attack.toString())
                    StatItem("Защита", "🛡️", creature.defense.toString())
                    StatItem("ХП", "❤️", creature.health.toString())
                    StatItem("Скор.", "🦶", creature.speed.toString())
                }

                Spacer(modifier = Modifier.height(12.dp))
                val damage = if (creature.minDamage == creature.maxDamage) "${creature.minDamage}" else "${creature.minDamage}-${creature.maxDamage}"
                Text("Урон: $damage", color = Color.White, fontWeight = FontWeight.Bold)

                if (creature.abilities.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Способности:", color = Color(0xFFD4AF37), fontWeight = FontWeight.Bold)
                    Text(
                        text = creature.abilities,
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}