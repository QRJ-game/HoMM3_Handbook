package com.example.homm3reference.ui.heroes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.homm3reference.data.Hero
import com.example.homm3reference.data.JSON_Mapper
import com.example.homm3reference.data.SecondarySkill
import com.example.homm3reference.data.Spell
import com.example.homm3reference.ui.common.*
import com.example.homm3reference.ui.theme.HommGlassBackground
import com.example.homm3reference.ui.theme.HommGold
import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import android.util.Log
import androidx.compose.runtime.LaunchedEffect

// Локальные константы стиля
private val HommShape = RoundedCornerShape(8.dp)
private val HommBorder = BorderStroke(2.dp, HommGold)

@Composable
fun ClassSelectionScreen(
    townName: String,
    mightClassName: String,
    magicClassName: String,
    onClassSelected: (String) -> Unit
) {
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    AppBackground {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            // --- ДОБАВИТЬ ---
            .padding(bottom = navBarPadding) // Чтобы контент не прилипал к навигации
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "$townName: Классы",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = HommGold,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                // Карточка Воина
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(160.dp)
                        .padding(end = 8.dp)
                        .clickable { onClassSelected("Might") },
                    colors = CardDefaults.cardColors(containerColor = HommGlassBackground),
                    border = HommBorder,
                    shape = HommShape
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⚔️", fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(mightClassName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = HommGold, textAlign = TextAlign.Center, lineHeight = 20.sp)
                            Text("Воин", fontSize = 16.sp, color = Color.White.copy(alpha = 0.9f))
                        }
                    }
                }

                // Карточка Мага
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(160.dp)
                        .padding(start = 8.dp)
                        .clickable { onClassSelected("Magic") },
                    colors = CardDefaults.cardColors(containerColor = HommGlassBackground),
                    border = HommBorder,
                    shape = HommShape
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⚡", fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(magicClassName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = HommGold, textAlign = TextAlign.Center, lineHeight = 20.sp)
                            Text("Маг", fontSize = 16.sp, color = Color.White.copy(alpha = 0.9f))
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
    listState: LazyListState = rememberLazyListState(),
    onHeroSelected: (Hero) -> Unit
) {
    // --- Логирование статистики списка героев и ошибок маппинга ---
    LaunchedEffect(heroes, townName, className) {
        val tag = "Homm3HeroesList"
        val standardCount = heroes.count { it.backgroundColor.isNullOrEmpty() }
        val specialCount = heroes.count { !it.backgroundColor.isNullOrEmpty() }

        Log.d(tag, "==========================================")
        Log.d(tag, "Список героев: $townName -> $className")
        Log.d(tag, "Всего в этом списке: ${heroes.size}")
        Log.d(tag, "------------------------------------------")
        Log.d(tag, "Обычные герои: $standardCount")
        Log.d(tag, "Специальные/Кампании: $specialCount")

        // 1. Проверка армии (поиск Fallback значения "creature_peasant")
        // Если маппер не находит существо, он подставляет "creature_peasant".
        val heroesWithPeasantFallback = heroes.filter { hero ->
            val army = JSON_Mapper.parseArmy(hero.army)
            army.any { it.first == "creature_peasant" }
        }

        if (heroesWithPeasantFallback.isNotEmpty()) {
            Log.e(tag, "⚠️ ВОЗМОЖНАЯ ОШИБКА АРМИИ (Fallback to Peasant):")
            heroesWithPeasantFallback.forEach { hero ->
                // Логируем имя героя и сырую строку армии для проверки
                Log.e(tag, "   -> Hero: ${hero.name} [${hero.town}], Army Raw: '${hero.army.replace("\n", ", ")}'")
            }
        }

        // 2. Проверка навыков (количество иконок < количества текстовых навыков)
        val heroesWithMissingSkills = heroes.filter { hero ->
            if (hero.skills.isBlank()) false else {
                val rawSkills = hero.skills.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                val mappedIcons = JSON_Mapper.getSkillIcons(hero.skills)
                // Если количество иконок не совпадает с количеством частей строки
                rawSkills.size != mappedIcons.size
            }
        }

        if (heroesWithMissingSkills.isNotEmpty()) {
            Log.e(tag, "⚠️ ОШИБКА МАППИНГА НАВЫКОВ (Не найдены иконки):")
            heroesWithMissingSkills.forEach { hero ->
                val rawSkills = hero.skills.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                // Определяем, какой именно навык отвалился, вызывая маппер для каждого отдельно
                val missingSkills = rawSkills.filter { skillName ->
                    JSON_Mapper.getSkillIcons(skillName).isEmpty()
                }
                Log.e(tag, "   -> Hero: ${hero.name}, Missing: $missingSkills (Raw: '${hero.skills}')")
            }
        }

        Log.d(tag, "==========================================")
    }
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val groupedHeroes = remember(heroes) {
        val standardHeroes = heroes.filter { it.backgroundColor.isNullOrEmpty() }
        val coloredHeroes = heroes.filter { !it.backgroundColor.isNullOrEmpty() }.groupBy { it.backgroundColor }
        Pair(standardHeroes, coloredHeroes)
    }

    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {

            Text(
                text = "$townName: $className",
                fontSize = 24.sp,
                color = HommGold,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding( top = 48.dp, bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            LazyColumn(
                // 4. Привязываем состояние
                state = listState,
                contentPadding = PaddingValues(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp + navBarPadding
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(groupedHeroes.first) { hero ->
                    HeroCard(hero, onHeroSelected)
                }

                // Разделитель, если есть и обычные, и специальные герои
                if (groupedHeroes.first.isNotEmpty() && groupedHeroes.second.isNotEmpty()) {
                    item { HorizontalDivider(color = HommGold, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp)) }
                }

                groupedHeroes.second.entries.forEachIndexed { index, entry ->
                    if (index > 0) {
                        item { HorizontalDivider(color = HommGold, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp)) }
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
    // Логика определения цвета фона (аналогично заклинаниям)
    val cardBackgroundColor = remember(hero.backgroundColor) {
        if (!hero.backgroundColor.isNullOrBlank()) {
            try {
                // Парсим цвет из hex-строки (например, "#DC143C")
                Color(android.graphics.Color.parseColor(hero.backgroundColor))
            } catch (e: Exception) {
                HommGlassBackground
            }
        } else {
            HommGlassBackground
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onHeroSelected(hero) },
        // Используем динамический цвет
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        //border = HommBorder,
        shape = HommShape
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeroImage(imageName = hero.imageRes, width = 58.dp, height = 64.dp)
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = hero.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = HommGold
            )
        }
    }
}

@Composable
fun HeroDetailScreen(hero: Hero, creatures: List<Creature>) {
    val stats = remember(hero.heroClass) { GameData.getStatsForClass(hero.heroClass) }
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
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
                .padding(
                    top = 32.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = navBarPadding
                )
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Шапка
                Row(verticalAlignment = Alignment.CenterVertically) {
                    HeroImage(
                        imageName = hero.imageRes,
                        width = 116.dp,
                        height = 128.dp,
                        borderWidth = 2.dp,
                        borderColor = HommGold
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        // Используем обычный текст, так как OutlinedText в Components может быть сложным,
                        // но для стиля используем золото
                        Text(
                            text = hero.name,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = HommGold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = hero.town, fontSize = 18.sp, color = Color.White)
                        Text(text = hero.heroClass, fontSize = 16.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = HommGold)

                // Статы
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatItem("Атака", "⚔️", stats.attack.toString())
                    StatItem("Защита", "🛡️", stats.defense.toString())
                    StatItem("Сила", "⚡", stats.power.toString())
                    StatItem("Знания", "📖", stats.knowledge.toString())
                }

                HorizontalDivider(modifier = Modifier.padding(top = 0.dp), color = HommGold)

                // Специализация
                SpecialtyInfoRow(
                    label = "Специализация",
                    value = hero.specialty,
                    iconRes = hero.specialtyIcon,
                    description = hero.specialtyDescription
                )

                HorizontalDivider(color = HommGold, modifier = Modifier.padding(vertical = 4.dp))

                // Навыки и Заклинание
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Левая колонка: Навыки
                    Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                        Text("Навыки", color = HommGold, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        Row(modifier = Modifier.padding(vertical = 8.dp)) {
                            val icons = remember(hero.skills) { JSON_Mapper.getSkillIcons(hero.skills) }

                            if (icons.isNotEmpty()) {
                                icons.forEach { iconName ->
                                    val skillId = iconName.substringAfter("_")
                                    val skill = allSkills.find { it.id == skillId } ?: allSkills.find { iconName.contains(it.id) }

                                    HeroImage(
                                        imageName = iconName,
                                        width = 60.dp,
                                        height = 60.dp,
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
                            lineHeight = 18.sp
                        )
                    }

                    // Правая колонка: Заклинание
                    Column(
                        modifier = Modifier.weight(0.8f).padding(start = 8.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "Заклинание",
                            color = HommGold,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (spellObj != null) {
                            Box(contentAlignment = Alignment.TopEnd, modifier = Modifier.fillMaxWidth()) {
                                HeroImage(
                                    imageName = spellObj.imageRes,
                                    width = 60.dp,
                                    height = 60.dp,
                                    modifier = Modifier.clickable { selectedSpell = spellObj }
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = spellObj.name,
                                color = Color.White,
                                fontSize = 14.sp,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Text(
                                text = hero.spell ?: "Нет",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp), color = HommGold)

                Text("Стартовая армия", color = HommGold, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(0.dp))

                ArmyVisuals(armyString = hero.army, onCreatureClick = { clickedImageRes ->
                    selectedCreature = creatures.find { it.imageRes == clickedImageRes }
                })

                Spacer(modifier = Modifier.height(32.dp + navBarPadding))
            }

            // POPUPS
            if (selectedCreature != null) {
                CreaturePopup(creature = selectedCreature!!, onDismiss = { selectedCreature = null })
            }
            if (selectedSkill != null) {
                SkillPopup(skill = selectedSkill!!, onDismiss = { selectedSkill = null })
            }
            if (selectedSpell != null) {
                SpellPopup(spell = selectedSpell!!, onDismiss = { selectedSpell = null })
            }
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun SpecialtyInfoRow(
    label: String,
    value: String,
    iconRes: String?,
    description: String?
) {
    val context = LocalContext.current

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            color = HommGold,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Иконка
            if (iconRes != null) {
                val isCreature = iconRes.startsWith("creature_")

                if (isCreature) {
                    val resId = remember(iconRes) {
                        context.resources.getIdentifier(iconRes, "drawable", context.packageName)
                    }

                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .width(60.dp)
                            .height(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.6f))
                            .border(2.dp, HommGold, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        if (resId != 0) {
                            Image(
                                painter = painterResource(id = resId),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                                    .offset(y = (-10).dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                } else {
                    HeroImage(
                        imageName = iconRes,
                        width = 60.dp,
                        height = 60.dp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }

            // Текст
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = value,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp
                )
                if (!description.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CreaturePopup(creature: Creature, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)) // Затемнение фона
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clickable(enabled = false) {},
            colors = CardDefaults.cardColors(containerColor = HommGlassBackground),
            //border = HommBorder,
            shape = HommShape,
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

                Text(creature.name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = HommGold)
                Text("Уровень ${creature.level}", fontSize = 16.sp, color = Color.Gray)

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = HommGold)

                val damage = if (creature.minDamage == creature.maxDamage) "${creature.minDamage}" else "${creature.minDamage}-${creature.maxDamage}"
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    StatItem("Атака", "⚔️", creature.attack.toString())
                    StatItem("Защита", "🛡️", creature.defense.toString())
                    StatItem("Урон", "💥️", damage)
                    StatItem("Здоровье", "❤️", creature.health.toString())
                    StatItem("Скор.", "🦶", creature.speed.toString())
                }

                if (creature.abilities.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Способности:", color = HommGold, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
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
            .background(Color.Black.copy(alpha = 0.8f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clickable(enabled = false) {},
            colors = CardDefaults.cardColors(containerColor = HommGlassBackground),
            //border = HommBorder,
            shape = HommShape
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(skill.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = HommGold)
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = HommGold)

                SkillPopupRow("Основной", skill.basic, "basic_${skill.id}")
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.White.copy(alpha=0.6f))
                SkillPopupRow("Продвинутый", skill.advanced, "advanced_${skill.id}")
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.White.copy(alpha=0.6f))
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
            .background(Color.Black.copy(alpha = 0.8f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .clickable(enabled = false) {},
            colors = CardDefaults.cardColors(containerColor = HommGlassBackground),
            border = HommBorder,
            shape = HommShape
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
                    color = HommGold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Уровень ${spell.level}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = HommGold)

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Ур.", modifier = Modifier.weight(1f), color = HommGold, fontWeight = FontWeight.Bold)
                    Text("Мана", modifier = Modifier.weight(0.8f), color = HommGold, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Text("Эффект", modifier = Modifier.weight(2.2f), color = HommGold, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }

                HorizontalDivider(color = Color.White, thickness = 1.dp)

                SpellPopupRow("Нет", spell.manaCostNone, spell.descriptionNone)
                HorizontalDivider(color = Color.White.copy(alpha = 0.6f))
                SpellPopupRow("Базовый", spell.manaCostBasic, spell.descriptionBasic)
                HorizontalDivider(color = Color.White.copy(alpha = 0.6f))
                SpellPopupRow("Продв.", spell.manaCostAdvanced, spell.descriptionAdvanced)
                HorizontalDivider(color = Color.White.copy(alpha = 0.6f))
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
        Text(text = level, modifier = Modifier.weight(1f), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Text(text = mana.toString(), modifier = Modifier.weight(0.8f), color = Color(0xFF4FC3F7), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, fontSize = 13.sp)
        Text(text = description, modifier = Modifier.weight(2.2f), color = Color.White, fontSize = 13.sp, lineHeight = 16.sp)
    }
}

@Composable
fun SkillPopupRow(level: String, description: String, imageRes: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            HeroImage(imageName = imageRes, width = 50.dp, height = 50.dp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = level, color = HommGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = description, color = Color.White, fontSize = 14.sp)
            }
        }
    }
}