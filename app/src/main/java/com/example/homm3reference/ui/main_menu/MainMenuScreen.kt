package com.example.homm3reference.ui.main_menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homm3reference.R
import com.example.homm3reference.ui.common.AppBackground
import com.example.homm3reference.ui.common.MenuButton
import com.example.homm3reference.ui.theme.HommGlassBackground
import com.example.homm3reference.ui.theme.HommGold

@Composable
fun MainMenuScreen(
    onHeroesClick: () -> Unit,
    onCreaturesClick: () -> Unit,
    onSkillsClick: () -> Unit,
    onMagicClick: () -> Unit,
    onArtifactsClick: () -> Unit,
    isMuted: Boolean,
    onMuteToggle: () -> Unit
) {
    var showAboutPopup by remember { mutableStateOf(false) }

    // Получаем конфигурацию экрана для вычисления высоты
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    AppBackground {
        Box(modifier = Modifier.fillMaxSize()) {

            // --- ОСНОВНОЙ КОНТЕНТ (Скролл) ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()), // Убрали паддинг сверху, чтобы картинка касалась края (если нужно)
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top // Начинаем сверху
            ) {
                // --- КОНТЕЙНЕР ДЛЯ КАРТИНКИ И КНОПКИ ЗВУКА ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp) // Внешний отступ
                        .height(screenHeight * 0.2f) // Ровно 20% высоты экрана
                        .clip(RoundedCornerShape(16.dp)) // Закругляем контейнер
                        //.border(2.dp, HommGold, RoundedCornerShape(16.dp)) // Золотая рамка
                ) {
                    // Картинка (Фон хедера)
                    Image(
                        painter = painterResource(id = R.drawable.top_header),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        // ContentScale.Crop - ключевой момент:
                        // Картинка заполнит контейнер, сохраняя пропорции.
                        // Лишнее по бокам (если формат 21:9) обрежется автоматически.
                        contentScale = ContentScale.Crop
                    )

                    // Кнопка музыки (Поверх картинки)
                    IconButton(
                        onClick = onMuteToggle,
                        modifier = Modifier
                            .align(Alignment.TopStart) // Левый верхний угол
                            .padding(8.dp) // Отступ внутри картинки
                            .size(40.dp)
                            .background(HommGlassBackground, CircleShape)
                            .border(1.dp, HommGold, CircleShape)
                    ) {
                        Text(
                            text = if (isMuted) "🔇" else "🔊",
                            fontSize = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- КНОПКИ МЕНЮ ---
                // Центрируем их в оставшемся пространстве, если нужно, или просто выводим списком
                Column(
                    modifier = Modifier.padding(bottom = 80.dp), // Отступ снизу, чтобы не наехать на кнопку "About"
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    MenuButton(text = "Герои", onClick = onHeroesClick)
                    MenuButton(text = "Существа", onClick = onCreaturesClick)
                    MenuButton(text = "Вторичные навыки", onClick = onSkillsClick)
                    MenuButton(text = "Магия", onClick = onMagicClick)
                    MenuButton(text = "Артефакты", onClick = onArtifactsClick)
                }
            }

            // --- КНОПКА "ОБ АВТОРЕ" ---
            // Она остается фиксированной в правом нижнем углу экрана
            IconButton(
                onClick = { showAboutPopup = true },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .size(48.dp)
                    .background(HommGlassBackground, CircleShape)
                    .border(2.dp, HommGold, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "About",
                    tint = HommGold
                )
            }

            // --- ВСПЛЫВАЮЩЕЕ ОКНО ---
            if (showAboutPopup) {
                AboutPopup(onDismiss = { showAboutPopup = false })
            }
        }
    }
}