package com.example.homm3reference.ui.main_menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homm3reference.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale

@Composable
fun AboutPopup(onDismiss: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val donateUrl = "https://www.donationalerts.com/r/sleo441"
    val telegramUrl = "https://t.me/QRJ_game"
    val emailUrl = "mailto:qrj.game@gmail.com"

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
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()), // Добавил скролл на случай маленьких экранов
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

                Spacer(modifier = Modifier.height(40.dp))

                // --- Блок 2 (Донат) ---
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

                Spacer(modifier = Modifier.height(20.dp))

                // --- Блок 3 (Контакты - НОВЫЙ) ---
                Text(
                    text = "Для предложений/улучшений/багов:",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD4AF37), // Золотой
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp)) // Отступ перед иконками

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val iconShape = RoundedCornerShape(8.dp) // Форма для иконок и рамки

                    // Иконка Telegram
                    Image(
                        painter = painterResource(id = R.drawable.social_tg_icon_small),
                        contentDescription = "Telegram Link",
                        contentScale = ContentScale.Crop, // Чтобы изображение заполнило всю область при обрезке
                        modifier = Modifier
                            .size(50.dp)
                            .clip(iconShape) // 1. Сначала обрезаем изображение
                            .border(2.dp, Color(0xFFD4AF37), iconShape) // 2. Рисуем рамку той же формы
                            .clickable { uriHandler.openUri(telegramUrl) }
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    // Иконка Email
                    Image(
                        painter = painterResource(id = R.drawable.social_mail_icon_small),
                        contentDescription = "Email Link",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(iconShape) // 1. Сначала обрезаем
                            .border(2.dp, Color(0xFFD4AF37), iconShape) // 2. Рисуем рамку
                            .clickable { uriHandler.openUri(emailUrl) }
                    )
                }
            }
        }
    }
}