package com.example.homm3reference.ui.skills

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue // ВАЖНО
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue // ВАЖНО
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homm3reference.data.SecondarySkill
import com.example.homm3reference.ui.common.AppBackground
import com.example.homm3reference.ui.common.AppSearchBar
import com.example.homm3reference.ui.common.HeroImage

@Composable
fun SecondarySkillsListScreen(
    skills: List<SecondarySkill>,
    onSkillSelected: (SecondarySkill) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    // Сортировка и фильтрация
    val displayedSkills = remember(skills, searchQuery) {
        val filtered = if (searchQuery.isBlank()) skills else skills.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }
        filtered.sortedBy { it.name }
    }

    AppBackground {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Вторичные навыки",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD4AF37),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Поиск
            AppSearchBar(
                query = searchQuery,
                onQueryChanged = { searchQuery = it },
                modifier = Modifier.padding(bottom = 16.dp),
                placeholderText = "Поиск навыка..."
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(displayedSkills) { skill ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onSkillSelected(skill) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HeroImage(imageName = "expert_${skill.id}", width = 48.dp, height = 48.dp)
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = skill.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SecondarySkillDetailScreen(skill: SecondarySkill) {
    AppBackground {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Кнопка "Назад" удалена

            Spacer(modifier = Modifier.height(16.dp))

            // Заголовок (иконка убрана, так как теперь они в уровнях)
            Text(
                text = skill.name,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD4AF37),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.White)

            // Уровни навыка с соответствующими иконками
            SkillLevelRow("Основной", skill.basic, "basic_${skill.id}")
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.White)
            SkillLevelRow("Продвинутый", skill.advanced, "advanced_${skill.id}")
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.White)
            SkillLevelRow("Эксперт", skill.expert, "expert_${skill.id}")
        }
    }
}

@Composable
fun SkillLevelRow(level: String, description: String, imageRes: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top // Выравнивание по верху
        ) {
            // Иконка уровня слева
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
