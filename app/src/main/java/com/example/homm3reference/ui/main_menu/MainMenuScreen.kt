package com.example.homm3reference.ui.main_menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    onSkillsClick: () -> Unit,
    onMagicClick: () -> Unit, // <-- Добавлен этот параметр
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
                Spacer(modifier = Modifier.height(20.dp))
                MenuButton(text = "Магия", onClick = onMagicClick) // <-- Добавлена кнопка

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
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
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