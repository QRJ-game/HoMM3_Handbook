package com.example.homm3reference.ui.skills

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homm3reference.data.SecondarySkill
import com.example.homm3reference.ui.common.AppBackground
import com.example.homm3reference.ui.common.AppSearchBar
import com.example.homm3reference.ui.theme.HommGlassBackground
import com.example.homm3reference.ui.theme.HommGold

// Константы стиля для этого экрана
private val HommShape = RoundedCornerShape(8.dp)
private val HommBorder = BorderStroke(2.dp, HommGold)

@Composable
fun SecondarySkillsListScreen(
    skills: List<SecondarySkill>,
    onSkillClick: (SecondarySkill) -> Unit,
    searchQuery: String,
    onQueryChanged: (String) -> Unit
) {
    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            // Заголовок и поиск
            Column(modifier = Modifier.padding(16.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Вторичные навыки",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = HommGold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                AppSearchBar(
                    query = searchQuery,
                    onQueryChanged = onQueryChanged,
                    placeholderText = "Поиск навыка..."
                )
            }

            // Список навыков
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(skills) { skill ->
                    SkillCard(skill = skill, onClick = { onSkillClick(skill) })
                }
            }
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun SkillCard(skill: SecondarySkill, onClick: () -> Unit) {
    // Получаем имя ресурса для экспертной иконки, чтобы показать её в списке (она самая красивая)
    val context = LocalContext.current
    val iconName = "expert_${skill.imageRes}"
    val iconId = remember(skill.imageRes) {
        context.resources.getIdentifier(iconName, "drawable", context.packageName)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = HommGlassBackground),
        //border = HommBorder,
        shape = HommShape,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка навыка
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.Black.copy(alpha = 0.3f), HommShape)
                    .border(2.dp, HommGold, HommShape)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                if (iconId != 0) {
                    Image(
                        painter = painterResource(id = iconId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Text("?", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Название
            Text(
                text = skill.name,
                color = HommGold,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SecondarySkillDetailScreen(skill: SecondarySkill) {
    AppBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Заголовок
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = skill.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = HommGold,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Уровни мастерства (Базовый, Продвинутый, Эксперт)
            item {
                SkillLevelCard(levelName = "Базовый", imageName = "basic_${skill.imageRes}", description = skill.basic)
                Spacer(modifier = Modifier.height(16.dp))
                SkillLevelCard(levelName = "Продвинутый", imageName = "advanced_${skill.imageRes}", description = skill.advanced)
                Spacer(modifier = Modifier.height(16.dp))
                SkillLevelCard(levelName = "Эксперт", imageName = "expert_${skill.imageRes}", description = skill.expert)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun SkillLevelCard(levelName: String, imageName: String, description: String) {
    val context = LocalContext.current
    val resId = remember(imageName) {
        context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = HommGlassBackground),
        //border = HommBorder,
        shape = HommShape
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Картинка уровня (слева)
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(Color.Black.copy(alpha = 0.3f), HommShape)
                    .border(2.dp, HommGold, HommShape)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                if (resId != 0) {
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Text("?", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Текст (справа)
            Column {
                Text(
                    text = levelName,
                    color = HommGold,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}