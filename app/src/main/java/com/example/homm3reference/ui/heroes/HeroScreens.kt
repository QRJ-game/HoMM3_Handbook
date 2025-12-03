package com.example.homm3reference.ui.heroes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val cardColor = if (!hero.backgroundColor.isNullOrEmpty()) {
        try { Color(android.graphics.Color.parseColor(hero.backgroundColor)) } catch (e: Exception) { MaterialTheme.colorScheme.surface }
    } else { MaterialTheme.colorScheme.surface }

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
fun HeroDetailScreen(hero: Hero, onBack: () -> Unit) {
    val stats = remember(hero.heroClass) { HeroBaseStats.getStats(hero.heroClass) }

    AppBackground {
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

            ArmyVisuals(armyString = hero.army)
        }
    }
}