package com.example.homm3reference.ui.skills

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homm3reference.data.SecondarySkill
import com.example.homm3reference.ui.common.AppBackground
import com.example.homm3reference.ui.common.HeroImage
import androidx.compose.material3.HorizontalDivider

@Composable
fun SecondarySkillsListScreen(
    skills: List<SecondarySkill>,
    onBack: () -> Unit,
    onSkillSelected: (SecondarySkill) -> Unit
) {
    AppBackground {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Кнопка "Назад" удалена.

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Вторичные навыки",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD4AF37),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(skills) { skill ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onSkillSelected(skill) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HeroImage(imageName = skill.imageRes, width = 48.dp, height = 48.dp)
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
fun SecondarySkillDetailScreen(skill: SecondarySkill, onBack: () -> Unit) {
    AppBackground {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Кнопка "Назад" удалена.

            Spacer(modifier = Modifier.height(16.dp))

            // Заголовок с иконкой
            Row(verticalAlignment = Alignment.CenterVertically) {
                HeroImage(imageName = skill.imageRes, width = 80.dp, height = 80.dp)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = skill.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD4AF37)
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.White)

            SkillLevelRow("Основной", skill.basic)
            SkillLevelRow("Продвинутый", skill.advanced)
            SkillLevelRow("Эксперт", skill.expert)
        }
    }
}

@Composable
fun SkillLevelRow(level: String, description: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = level, color = Color(0xFFD4AF37), fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = description, color = Color.White, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.White)
    }
}