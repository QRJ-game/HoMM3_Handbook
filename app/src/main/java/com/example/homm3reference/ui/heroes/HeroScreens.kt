package com.example.homm3reference.ui.heroes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.homm3reference.data.Creature
import com.example.homm3reference.data.GameData
import com.example.homm3reference.data.Hero
import com.example.homm3reference.data.JSON_Mapper
import com.example.homm3reference.data.SecondarySkill
import com.example.homm3reference.data.Spell
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
            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка Назад убрана, оставлен только заголовок
            Text(
                text = "$townName: Классы",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD4AF37),
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

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

            // Кнопка Назад убрана
            Text(
                text = "$townName • $className",
                fontSize = 20.sp,
                color = Color(0xFFD4AF37),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp)
            )

            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(groupedHeroes.first) { hero ->
                    HeroCard(hero, onHeroSelected)
                }
                if (groupedHeroes.first.isNotEmpty() && groupedHeroes.second.isNotEmpty()) {
                    item { HorizontalDivider(color = Color.White, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp)) }
                }
                groupedHeroes.second.entries.forEachIndexed { index, entry ->
                    if (index > 0) {
                        item { HorizontalDivider(color = Color.White, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp)) }
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
            HeroImage(imageName = hero.imageRes, width = 58.dp, height = 64.dp)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = hero.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun HeroDetailScreen(hero: Hero, creatures: List<Creature>, onBack: () -> Unit) {
    val stats = remember(hero.heroClass) { GameData.getStatsForClass(hero.heroClass) }

    val spellObj = remember(hero.spell) {
        GameData.spells.find { it.name.equals(hero.spell, ignoreCase = true) }
    }

    var selectedCreature by remember { mutableStateOf<Creature?>(null) }
    var selectedSkill by remember { mutableStateOf<SecondarySkill?>(null) }
    var selectedSpell by remember { mutableStateOf<Spell?>(null) }

    val allSkills = remember { GameData.secondarySkills }

    AppBackground {
        Box(modifier = Modifier.fillMaxSize()) {

            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    HeroImage(
                        imageName = hero.imageRes,
                        width = 116.dp,
                        height = 128.dp,
                        borderWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        OutlinedText(
                            text = hero.name,
                            fontSize = 32.sp,
                            strokeWidth = 6f,
                            textColor =  Color(0xFFD4AF37)
                        )
                        Text(text = hero.town, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = hero.heroClass, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.9f))
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color.White)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatItem("Атака", "⚔️", stats.attack.toString())
                    StatItem("Защита", "🛡️", stats.defense.toString())
                    StatItem("Сила", "⚡", stats.power.toString())
                    StatItem("Знания", "📖", stats.knowledge.toString())
                }

                HorizontalDivider(color = Color.White)

                InfoRow("Специализация", hero.specialty)

                HorizontalDivider(color = Color.White, modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Левая колонка: Навыки
                    Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                        Text("Навыки", color = Color(0xFFD4AF37), fontSize = 16.sp, fontWeight = FontWeight.Bold)

                        Row(modifier = Modifier.padding(vertical = 6.dp)) {
                            val icons = remember(hero.skills) { JSON_Mapper.getSkillIcons(hero.skills) }

                            if (icons.isNotEmpty()) {
                                icons.forEach { iconName ->
                                    val skillId = iconName.substringAfter("_")
                                    val skill = allSkills.find { it.id == skillId } ?: allSkills.find { iconName.contains(it.id) }

                                    HeroImage(
                                        imageName = iconName,
                                        width = 60.dp,
                                        height = 68.dp,
                                        modifier = Modifier
                                            .padding(end = 6.dp)
                                            .clickable(enabled = skill != null) {
                                                if (skill != null) selectedSkill = skill
                                            }
                                    )
                                }
                            }
                        }
                        Text(
                            text = hero.skills,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 16.sp
                        )
                    }

                    // Правая колонка: Заклинание
                    Column(
                        modifier = Modifier.weight(0.8f).padding(start = 8.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "Заклинание",
                            color = Color(0xFFD4AF37),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        if (spellObj != null) {
                            HeroImage(
                                imageName = spellObj.imageRes,
                                width = 60.dp,
                                height = 68.dp,
                                modifier = Modifier.clickable { selectedSpell = spellObj }
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = spellObj.name,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.End
                            )
                        } else {
                            Text(
                                text = hero.spell ?: "Нет",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(top = 12.dp), color = Color.White)

                Spacer(modifier = Modifier.height(10.dp))
                Text("Стартовая армия", color = Color(0xFFD4AF37), fontWeight = FontWeight.Black, fontSize = 16.sp)

                ArmyVisuals(armyString = hero.army, onCreatureClick = { clickedImageRes ->
                    selectedCreature = creatures.find { it.imageRes == clickedImageRes }
                })

                Spacer(modifier = Modifier.height(32.dp))
            }

            // 2. POPUPS

            if (selectedCreature != null) {
                CreaturePopup(
                    creature = selectedCreature!!,
                    onDismiss = { selectedCreature = null }
                )
            }

            if (selectedSkill != null) {
                SkillPopup(
                    skill = selectedSkill!!,
                    onDismiss = { selectedSkill = null }
                )
            }

            if (selectedSpell != null) {
                SpellPopup(
                    spell = selectedSpell!!,
                    onDismiss = { selectedSpell = null }
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
                val damage = if (creature.minDamage == creature.maxDamage) "${creature.minDamage}" else "${creature.minDamage}-${creature.maxDamage}"
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    StatItem("Атака", "⚔️", creature.attack.toString())
                    StatItem("Защита", "🛡️", creature.defense.toString())
                    StatItem("Урон", "💥️", damage)
                    StatItem("     ХП     ", "❤️", creature.health.toString())
                    StatItem("Скор. ", "🦶", creature.speed.toString())
                }

                Spacer(modifier = Modifier.height(12.dp))

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

@Composable
fun SkillPopup(skill: SecondarySkill, onDismiss: () -> Unit) {
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
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(skill.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.White)

                SkillPopupRow("Основной", skill.basic, "basic_${skill.id}")
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.White)
                SkillPopupRow("Продвинутый", skill.advanced, "advanced_${skill.id}")
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.White)
                SkillPopupRow("Эксперт", skill.expert, "expert_${skill.id}")
            }
        }
    }
}

@Composable
fun SpellPopup(spell: Spell, onDismiss: () -> Unit) {
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
                .wrapContentHeight() // Высота подстраивается под контент
                .clickable(enabled = false) {},
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeroImage(imageName = spell.imageRes, width = 80.dp, height = 80.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = spell.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD4AF37),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Уровень ${spell.level}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.White)

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Навык", modifier = Modifier.weight(0.8f), color = Color(0xFFD4AF37), fontWeight = FontWeight.Bold)
                    Text("Мана", modifier = Modifier.weight(0.8f), color = Color(0xFFD4AF37), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Text("Эффект", modifier = Modifier.weight(2.4f), color = Color(0xFFD4AF37), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }

                HorizontalDivider(color = Color.Gray, thickness = 1.dp)

                SpellPopupRow("Нет", spell.manaCostNone, spell.descriptionNone)
                HorizontalDivider(color = Color.Gray, thickness = 0.5.dp)
                SpellPopupRow("Базовый", spell.manaCostBasic, spell.descriptionBasic)
                HorizontalDivider(color = Color.Gray, thickness = 0.5.dp)
                SpellPopupRow("Продв.", spell.manaCostAdvanced, spell.descriptionAdvanced)
                HorizontalDivider(color = Color.Gray, thickness = 0.5.dp)
                SpellPopupRow("Эксперт", spell.manaCostExpert, spell.descriptionExpert)
            }
        }
    }
}

@Composable
fun SpellPopupRow(level: String, mana: Int, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = level,
            modifier = Modifier.weight(1f),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Text(
            text = mana.toString(),
            modifier = Modifier.weight(0.5f),
            color = Color(0xFF4FC3F7),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
        Text(
            text = description,
            modifier = Modifier.weight(2.5f),
            color = Color.White,
            fontSize = 14.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
fun SkillPopupRow(level: String, description: String, imageRes: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            HeroImage(imageName = imageRes, width = 64.dp, height = 64.dp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = level, color = Color(0xFFD4AF37), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = description, color = Color.White, fontSize = 16.sp)
            }
        }
    }
}