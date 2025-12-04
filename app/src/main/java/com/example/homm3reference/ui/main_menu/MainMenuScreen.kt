package com.example.homm3reference.ui.main_menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.* // Импортируем для mutableStateOf, remember, getValue, setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homm3reference.R
import com.example.homm3reference.ui.common.AppBackground
import com.example.homm3reference.ui.common.MenuButton
import androidx.compose.ui.text.font.FontStyle


@Composable
fun MainMenuScreen(
    onHeroesClick: () -> Unit,
    onCreaturesClick: () -> Unit,
    onSkillsClick: () -> Unit,
    isMuted: Boolean,
    onMuteToggle: () -> Unit
) {
    // Состояние для отображения всплывающего окна "Об авторе"
    var showAboutPopup by remember { mutableStateOf(false) }

    AppBackground {
        Box(modifier = Modifier.fillMaxSize()) {

            // --- ОСНОВНОЙ КОНТЕНТ (Скроллящийся) ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
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
                Spacer(modifier = Modifier.height(20.dp))
                MenuButton(text = "Вторичные навыки", onClick = onSkillsClick)

                Spacer(modifier = Modifier.height(32.dp))
            }

            // --- КНОПКА МУЗЫКИ (Верхний левый угол) ---
            IconButton(
                onClick = onMuteToggle,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Text(
                    text = if (isMuted) "🔇" else "🔊",
                    fontSize = 24.sp
                )
            }

            // --- КНОПКА "ОБ АВТОРЕ" (Нижний правый угол) ---
            IconButton(
                onClick = { showAboutPopup = true },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd) // Прибиваем к правому нижнему углу
                    .navigationBarsPadding() // Отступ от системной навигации снизу
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Text(
                    text = "❓",
                    fontSize = 24.sp,
                    color = Color.White
                )
            }

            // --- ВСПЛЫВАЮЩЕЕ ОКНО ---
            if (showAboutPopup) {
                AboutPopup(onDismiss = { showAboutPopup = false })
            }
        }
    }
}

@Composable
fun AboutPopup(onDismiss: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val donateUrl = "https://www.donationalerts.com/r/sleo441"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)) // Затемнение фона
            .clickable { onDismiss() }, // Закрытие при клике мимо
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f) // Чуть шире, чтобы текст влезал
                .clickable(enabled = false) {}, // Блокируем клик, чтобы окно не закрылось при нажатии на карточку
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- Блок 1 ---
                Text(
                    text = "Огромное спасибо:",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD4AF37), // Золотой
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Разработчикам Heroes of Might and Magic III",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "За лучшую игру и наше счастливое детство\n",
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFFD4AF37),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Разрабочикам HotA",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "За лучшее продолжение лучшей игры\n",
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFFD4AF37),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Создателям FizMiG",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Потому что Книга всегда лучше!\n",
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color(0xFFD4AF37),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                // --- Блок 2 ---
                Text(
                    text = "Поддержать автора:",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD4AF37), // Золотой
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable { uriHandler.openUri(donateUrl) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = donateUrl,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textDecoration = TextDecoration.Underline, // Подчеркивание
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable { uriHandler.openUri(donateUrl) }
                )
            }
        }
    }
}