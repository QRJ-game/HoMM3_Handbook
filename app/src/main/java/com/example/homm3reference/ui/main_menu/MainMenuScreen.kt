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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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

    AppBackground {
        Box(modifier = Modifier.fillMaxSize()) {

            // --- ОСНОВНОЙ КОНТЕНТ ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Логотип/Картинка сверху с рамкой
                Image(
                    painter = painterResource(id = R.drawable.main_top),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .heightIn(max = 250.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, HommGold, RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Кнопки меню
                MenuButton(text = "Герои", onClick = onHeroesClick)
                Spacer(modifier = Modifier.height(16.dp))
                MenuButton(text = "Существа", onClick = onCreaturesClick)
                Spacer(modifier = Modifier.height(16.dp))
                MenuButton(text = "Вторичные навыки", onClick = onSkillsClick)
                Spacer(modifier = Modifier.height(16.dp))
                MenuButton(text = "Магия", onClick = onMagicClick)
                Spacer(modifier = Modifier.height(16.dp))
                MenuButton(text = "Артефакты", onClick = onArtifactsClick)

                Spacer(modifier = Modifier.height(48.dp))
            }

            // --- КНОПКА МУЗЫКИ ---
            IconButton(
                onClick = onMuteToggle,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .size(48.dp)
                    .background(HommGlassBackground, CircleShape)
                    .border(2.dp, HommGold, CircleShape)
            ) {
                // Используем emoji, так как нужных иконок нет в базовом наборе Compose
                Text(
                    text = if (isMuted) "🔇" else "🔊",
                    fontSize = 24.sp
                )
            }

            // --- КНОПКА "ОБ АВТОРЕ" ---
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
                    imageVector = Icons.Default.Info, // Эта иконка есть в стандарте
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