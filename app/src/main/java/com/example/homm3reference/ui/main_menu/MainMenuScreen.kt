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
import com.example.homm3reference.ui.common.HommListCard
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import com.example.homm3reference.BuildConfig


@Composable
fun MainMenuScreen(
    onHeroesClick: () -> Unit,
    onCreaturesClick: () -> Unit,
    onSkillsClick: () -> Unit,
    onMagicClick: () -> Unit,
    onArtifactsClick: () -> Unit,
    onUtilitiesClick: () -> Unit,
    isMuted: Boolean,
    onMuteToggle: () -> Unit
) {
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
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
                // --- КОНТЕЙНЕР ДЛЯ КАРТИНКИ И КНОПКИ ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 0.33f)
                ) {
                    // 1. Картинка (Без изменений)
                    Image(
                        painter = painterResource(id = R.drawable.top_header),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                            .drawWithContent {
                                drawContent()
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        0f to Color.Black,
                                        0.7f to Color.Black,
                                        1f to Color.Transparent
                                    ),
                                    blendMode = BlendMode.DstIn
                                )
                            }
                    )

                    // 2. Кнопка музыки (Закрываем скобку кнопки ПЕРЕД версиями)
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
                    } // <--- ВОТ ЗДЕСЬ заканчивается кнопка

                    // 3. Теперь надписи (Они находятся внутри Box, но ПОСЛЕ кнопки)

                    // Снизу слева: Версия приложения
                    Text(
                        text = "Версия справочника ${BuildConfig.VERSION_NAME}",
                        color = HommGold,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, bottom = 16.dp)
                    )

                    // Снизу справа: Версия HotA
                    Text(
                        text = "Версия HotA 1.8.0",
                        color = HommGold,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 16.dp, bottom = 16.dp)
                    )
                } // <--- ВОТ ЗДЕСЬ заканчивается контейнер заголовка (Box)

                //Spacer(modifier = Modifier.height(16.dp))

                // --- КНОПКИ МЕНЮ ---
                Column(
                   // modifier = Modifier.padding(bottom = 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val menuPadding = Modifier.padding(horizontal = 16.dp, vertical = 0.dp)
                    HommListCard(
                        text = "Герои",
                        imageRes = "hero_catherine_menu",
                        onClick = onHeroesClick,
                        modifier = menuPadding
                    )

                    HommListCard(
                        text = "Существа",
                        imageRes = "creature_champion",
                        onClick = onCreaturesClick,
                        modifier = menuPadding
                    )

                    HommListCard(
                        text = "Вторичные навыки",
                        imageRes = "expert_tactics",
                        onClick = onSkillsClick,
                        modifier = menuPadding
                    )

                    HommListCard(
                        text = "Магия",
                        imageRes = "spell_lightning_bolt_menu",
                        onClick = onMagicClick,
                        modifier = menuPadding
                    )

                    HommListCard(
                        text = "Артефакты",
                        imageRes = "artifact_centaurs_axe",
                        onClick = onArtifactsClick,
                        modifier = menuPadding
                    )
                    HommListCard(
                        text = "Утилиты",
                        imageRes = "menu_utils", // Убедитесь, что файл menu_utils.png добавлен в drawable, иначе используйте заглушку
                        onClick = onUtilitiesClick,
                        modifier = menuPadding
                    )
                }
                Spacer(modifier = Modifier.height(navBarPadding + 80.dp))
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