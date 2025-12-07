package com.example.homm3reference.ui.magic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homm3reference.data.Spell
import com.example.homm3reference.ui.common.AppBackground
import com.example.homm3reference.ui.common.HeroImage

@Composable
fun MagicSchoolSelectScreen(
    onBack: () -> Unit,
    onSchoolSelected: (String) -> Unit
) {
    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Школы Магии",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD4AF37),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Сетка 2x2
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                MagicSchoolCard("Земля", "expert_earth_magic", "Earth", onSchoolSelected)
                MagicSchoolCard("Вода", "expert_water_magic", "Water", onSchoolSelected)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                MagicSchoolCard("Огонь", "expert_fire_magic", "Fire", onSchoolSelected)
                MagicSchoolCard("Воздух", "expert_air_magic", "Air", onSchoolSelected)
            }
        }
    }
}

@Composable
fun MagicSchoolCard(name: String, icon: String, schoolId: String, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .size(160.dp)
            .clickable { onClick(schoolId) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HeroImage(imageName = icon, width = 100.dp, height = 100.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun SpellListScreen(
    school: String,
    spells: List<Spell>,
    onBack: () -> Unit,
    onSpellSelected: (Spell) -> Unit
) {
    val schoolName = when(school) {
        "Earth" -> "Магия Земли"
        "Water" -> "Магия Воды"
        "Fire" -> "Магия Огня"
        "Air" -> "Магия Воздуха"
        else -> "Заклинания"
    }

    // Группируем заклинания по уровню (1..5) и сортируем внутри группы по алфавиту
    val groupedSpells = remember(spells) {
        spells
            .groupBy { it.level }
            .toSortedMap() // Сортируем ключи (уровни) по возрастанию
            .mapValues { entry ->
                entry.value.sortedBy { it.name } // Сортируем списки заклинаний по имени
            }
    }

    AppBackground {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = schoolName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD4AF37),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Проходимся по каждой группе (Уровень -> Список заклинаний)
                groupedSpells.forEach { (level, levelSpells) ->



                    // Список заклинаний этого уровня
                    items(levelSpells) { spell ->
                        // Определяем цвет фона карточки
                        val cardColor = if (spell.backgroundColor != null) {
                            try {
                                Color(android.graphics.Color.parseColor(spell.backgroundColor))
                            } catch (e: Exception) {
                                MaterialTheme.colorScheme.surface // Если ошибка парсинга
                            }
                        } else {
                            MaterialTheme.colorScheme.surface // Стандартный цвет
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSpellSelected(spell) },
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Иконка заклинания в списке
                                HeroImage(imageName = spell.imageRes, width = 48.dp, height = 48.dp)
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = spell.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Уровень: ${spell.level} | Мана: ${spell.manaCostBasic}",
                                        fontSize = 14.sp,
                                        color = Color.White // Можно заменить на Color.White.copy(alpha = 0.8f) для контраста на цветном фоне
                                    )
                                }
                            }
                        }
                    }

                    // Белый разделитель после каждого уровня
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(thickness = 1.dp, color = Color.White)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}
@Composable
fun SpellDetailScreen(spell: Spell, onBack: () -> Unit) {
    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // --- ШАПКА: Иконка и Название ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HeroImage(imageName = spell.imageRes, width = 80.dp, height = 80.dp)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = spell.name,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD4AF37)
                    )
                    Text(
                        text = "${mapSchoolName(spell.school)}\n${spell.level} уровень",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.White)

            // --- ТАБЛИЦА: Уровень | Мана | Описание ---

            // Заголовки таблицы (опционально)
            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                Text(text = "Навык", modifier = Modifier.weight(1f), color = Color(0xFFD4AF37), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "Мана", modifier = Modifier.weight(0.6f), color = Color(0xFFD4AF37), fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center)
                Text(text = "Эффект", modifier = Modifier.weight(2.5f), color = Color(0xFFD4AF37), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            HorizontalDivider(color = Color.White, thickness = 1.dp)

            // Строки
            SpellEffectRow("Нет", spell.manaCostNone, spell.descriptionNone)
            HorizontalDivider(color = Color.White)

            SpellEffectRow("Базовый", spell.manaCostBasic, spell.descriptionBasic)
            HorizontalDivider(color = Color.White)

            SpellEffectRow("Продвинутый", spell.manaCostAdvanced, spell.descriptionAdvanced)
            HorizontalDivider(color = Color.White)

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
        // Столбец 1: Уровень навыка
        Text(
            text = levelName,
            modifier = Modifier.weight(1f),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        // Столбец 2: Мана
        Text(
            text = mana.toString(),
            modifier = Modifier.weight(0.6f),
            color = Color(0xFF4FC3F7), // Голубоватый цвет для маны
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )

        // Столбец 3: Описание
        Text(
            text = description,
            modifier = Modifier.weight(2.5f),
            color = Color.White,
            fontSize = 16.sp,
            lineHeight = 20.sp
        )
    }
}

// Вспомогательная функция для перевода названия школы
fun mapSchoolName(schoolId: String): String {
    return when(schoolId) {
        "Earth" -> "Магия Земли"
        "Air" -> "Магия Воздуха"
        "Fire" -> "Магия Огня"
        "Water" -> "Магия Воды"
        else -> schoolId
    }
}