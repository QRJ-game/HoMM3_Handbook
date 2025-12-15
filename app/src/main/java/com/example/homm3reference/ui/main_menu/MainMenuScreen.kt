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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.drawWithContent


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

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    AppBackground {
        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // --- КОНТЕЙНЕР ДЛЯ КАРТИНКИ И КНОПКИ ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 0.25f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.top_header),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            // 1. Создаем отдельный слой для отрисовки, чтобы смешивание цветов
                            // работало только для этой картинки, а не вырезало дыру до черного экрана.
                            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                            // 2. Используем drawWithContent для наложения маски
                            .drawWithContent {
                                // Сначала рисуем саму картинку
                                drawContent()

                                // Затем рисуем градиент поверх с режимом DstIn.
                                // В этом режиме:
                                // Color.Black (непрозрачный) = картинка ВИДНА
                                // Color.Transparent = картинка НЕ ВИДНА
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        0f to Color.Black,        // Верх картинки полностью видим
                                        0.7f to Color.Black,      // До 30% высоты полная видимость
                                        1f to Color.Transparent   // К самому низу полностью исчезает
                                    ),
                                    blendMode = BlendMode.DstIn
                                )
                            }
                    )

                    // Кнопка музыки (Поверх всего)
                    IconButton(
                        onClick = onMuteToggle,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .statusBarsPadding()
                            .padding(8.dp)
                            .size(40.dp)
                            .background(HommGlassBackground.copy(alpha = 0.6f), CircleShape)
                            .border(1.dp, HommGold, CircleShape)
                    ) {
                        Text(
                            text = if (isMuted) "🔇" else "🔊",
                            fontSize = 20.sp
                        )
                    }

                    // Опционально: Можно оставить золотую линию внизу для стиля,
                    // или убрать её, если хотите просто плавное растворение в фоне.
                    /*
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(HommGold)
                    )
                    */
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- КНОПКИ МЕНЮ ---
                Column(
                    modifier = Modifier.padding(bottom = 80.dp),
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

            // ... (Код кнопки "Об авторе" и попапа остается без изменений) ...
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

            if (showAboutPopup) {
                AboutPopup(onDismiss = { showAboutPopup = false })
            }
        }
    }
}