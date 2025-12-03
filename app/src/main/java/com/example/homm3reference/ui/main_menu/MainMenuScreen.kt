package com.example.homm3reference.ui.main_menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState // 1. Импорт для скролла
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll // 2. Импорт модификатора
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homm3reference.R
import com.example.homm3reference.ui.common.AppBackground
import com.example.homm3reference.ui.common.MenuButton

@Composable
fun MainMenuScreen(
    onHeroesClick: () -> Unit,
    onCreaturesClick: () -> Unit,
    isMuted: Boolean,
    onMuteToggle: () -> Unit
) {
    AppBackground {
        // Box нужен, чтобы наложить кнопку звука ПОВЕРХ прокручиваемого списка
        Box(modifier = Modifier.fillMaxSize()) {

            // --- ОСНОВНОЙ КОНТЕНТ (Скроллящийся) ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // <--- ВКЛЮЧАЕМ СКРОЛЛ
                    .padding(vertical = 16.dp), // Отступы сверху/снизу для безопасности
                horizontalAlignment = Alignment.CenterHorizontally,
                // Arrangement.Center работает хитро:
                // Если контент влезает -> центрирует его.
                // Если контент НЕ влезает (как в Landscape) -> начинает скролл сверху.
                verticalArrangement = Arrangement.Center
            ) {
                // Верхнее изображение
                Image(
                    painter = painterResource(id = R.drawable.main_top),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .heightIn(max = 250.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(32.dp))

                MenuButton(text = "Герои", onClick = onHeroesClick)
                Spacer(modifier = Modifier.height(20.dp))
                MenuButton(text = "Существа", onClick = onCreaturesClick)

                // Дополнительный отступ снизу, чтобы скролл не обрезал кнопку впритык
                Spacer(modifier = Modifier.height(32.dp))
            }

            // --- КНОПКА МУЗЫКИ (Поверх скролла) ---
            // Мы вынесли её из Column, чтобы она всегда висела в углу и не уезжала
            IconButton(
                onClick = onMuteToggle,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart) // Прибиваем к левому верхнему углу
                    // Статус бар и вырезы экрана (safe drawing) лучше обрабатывать Scaffold-ом,
                    // но пока оставим паддинг. В Landscape лучше добавить windowInsetsPadding,
                    // но для начала хватит простого padding.
                    .statusBarsPadding() // <--- Лайфхак: отступ под системную строку (часы/батарейка)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Text(
                    text = if (isMuted) "🔇" else "🔊",
                    fontSize = 24.sp
                )
            }
        }
    }
}