package com.example.homm3reference.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homm3reference.data.ArmyMapper

/**
 * Общий компонент фона приложения.
 * Рисует картинку "main_background" и накладывает затемнение.
 */
@Composable
fun AppBackground(
    content: @Composable BoxScope.() -> Unit
) {
    val context = LocalContext.current
    val bgId = remember(Unit) {
        context.resources.getIdentifier("main_background", "drawable", context.packageName)
    }

    // Матрица цветов для создания эффекта "негатива" (инверсия цветов).
    // Используется, чтобы сделать светлый пергамент темным.
    val colorMatrix = floatArrayOf(
        -1f, 0f, 0f, 0f, 255f,
        0f, -1f, 0f, 0f, 255f,
        0f, 0f, -1f, 0f, 255f,
        0f, 0f, 0f, 1f, 0f
    )

    Box(modifier = Modifier.fillMaxSize()) {
        if (bgId != 0) {
            Image(
                painter = painterResource(id = bgId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                // Применяем фильтр инверсии. Если убрать эту строку, фон будет светлым.
                colorFilter = ColorFilter.colorMatrix(ColorMatrix(colorMatrix))
            )
            // Слой затемнения поверх картинки.
            // alpha = 0.2f означает 20% прозрачности черного цвета.
            // Можно увеличить (например, до 0.5f), чтобы фон был темнее.
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.2f)))
        } else {
            // Запасной вариант: просто темный фон, если картинка не найдена
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))
        }

        content()
    }
}

/**
 * Стандартная кнопка меню.
 */
@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        // Размер кнопки: ширина 250dp, высота 60dp
        modifier = Modifier.width(250.dp).height(60.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        // Размер текста кнопки (22.sp) и цвет (Золотой)
        Text(text, fontSize = 22.sp, color = Color(0xFFD4AF37), fontWeight = FontWeight.Bold)
    }
}

/**
 * Карточка выбора города в меню.
 */
@Composable
fun TownCard(townName: String, onClick: () -> Unit) {
    val context = LocalContext.current

    // Сопоставление названия города и имени файла картинки
    val imageResName = when(townName) {
        "Замок" -> "town_castle"
        "Оплот" -> "town_rampart"
        "Башня" -> "town_tower"
        "Инферно" -> "town_inferno"
        "Некрополис" -> "town_necropolis"
        "Темница" -> "town_dungeon"
        "Цитадель" -> "town_stronghold"
        "Крепость" -> "town_fortress"
        "Сопряжение" -> "town_conflux"
        "Причал" -> "town_cove"
        "Фабрика" -> "town_factory"
        "Нейтралы" -> "creature_azure_dragon"
        "Боевые машины" -> "creature_ballista"
        else -> "icon_image"
    }

    val resId = remember(imageResName) {
        context.resources.getIdentifier(imageResName, "drawable", context.packageName)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f) // Пропорция ширины к высоте (3:2). Меняйте, если нужно выше/ниже.
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (resId != 0) {
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Градиентное затемнение снизу, чтобы текст читался
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f)),
                                startY = 100f
                            )
                        )
                )
            } else {
                Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray))
            }

            // Текст названия города
            Text(
                text = townName,
                fontSize = 20.sp, // <-- РАЗМЕР ШРИФТА НАЗВАНИЯ ГОРОДА
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Универсальный компонент для картинки (героя или существа).
 * Поддерживает настройку размеров и рамки.
 */
@Composable
fun HeroImage(
    imageName: String,
    width: Dp = 64.dp, // Ширина по умолчанию
    height: Dp = 64.dp, // Высота по умолчанию
    contentScale: ContentScale = ContentScale.Crop, // Как вписывать картинку (Crop - обрезать, Fit - вписать)
    borderWidth: Dp = 1.dp, // Толщина рамки
    borderColor: Color = Color(0xFFD4AF37) // Цвет рамки (золотой)
) {
    val context = LocalContext.current
    val resId = remember(imageName) {
        context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }

    if (resId != 0) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            modifier = Modifier
                .width(width)
                .height(height)
                .clip(RoundedCornerShape(8.dp)) // Скругление углов
                .border(borderWidth, borderColor, RoundedCornerShape(8.dp)), // Рамка
            contentScale = contentScale
        )
    } else {
        // Заглушка, если картинки нет
        Box(
            modifier = Modifier
                .width(width)
                .height(height)
                .background(Color.Gray)
                .clip(RoundedCornerShape(8.dp))
                .border(borderWidth, borderColor, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("?", color = Color.White)
        }
    }
}

/**
 * Элемент статистики (Атака, Защита и т.д.) с иконкой.
 */
@Composable
fun StatItem(label: String, icon: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Размер иконки (эмодзи)
        Text(icon, fontSize = 24.sp)

        // Значение стата (Цифра)
        Text(
            text = value,
            fontSize = 22.sp, // <-- РАЗМЕР ЦИФРЫ
            fontWeight = FontWeight.Black, // Жирность (Black - очень жирный)
            color = Color.White
        )

        // Подпись (Название стата)
        Text(
            text = label,
            fontSize = 14.sp, // <-- РАЗМЕР ПОДПИСИ
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(alpha = 0.8f) // Немного прозрачный белый
        )
    }
}

/**
 * Строка информации (Цена, Прирост и т.д.)
 */
@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        // Заголовок (Название параметра)
        Text(
            text = label,
            color = Color(0xFFD4AF37), // Золотой цвет
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp // <-- РАЗМЕР ЗАГОЛОВКА
        )
        // Значение параметра
        Text(
            text = value,
            fontWeight = FontWeight.Medium,
            color = Color.White, // Белый цвет
            fontSize = 16.sp // <-- РАЗМЕР ТЕКСТА ЗНАЧЕНИЯ
        )
    }
}

/**
 * Блок отрисовки стартовой армии.
 */
@Composable
fun ArmyVisuals(armyString: String) {
    val armyList = remember(armyString) { ArmyMapper.parseArmy(armyString) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        armyList.forEach { (imageRes, count) ->
            ArmySlot(imageRes, count)
        }
    }
}

/**
 * Одиночный слот армии (Картинка + Количество).
 */
@Composable
fun ArmySlot(imageRes: String, count: String) {
    val context = LocalContext.current
    val resId = remember(imageRes) {
        context.resources.getIdentifier(imageRes, "drawable", context.packageName)
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.padding(4.dp)
    ) {
        if (resId != 0) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = null,
                modifier = Modifier
                    .width(100.dp)  // <-- ШИРИНА КАРТИНКИ ЮНИТА
                    .height(130.dp) // <-- ВЫСОТА КАРТИНКИ ЮНИТА
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.DarkGray)
                    .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(8.dp)), // Толщина рамки 2.dp
                contentScale = ContentScale.Fit // Вписываем картинку целиком
            )
        } else {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(130.dp)
                    .background(Color.Gray, RoundedCornerShape(8.dp))
                    .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(8.dp))
            )
        }

        Box(
            modifier = Modifier.offset(y = 8.dp) // Смещение текста вниз
        ) {
            // Размер шрифта количества юнитов
            OutlinedText(text = count, fontSize = 14.sp)
        }
    }
}

/**
 * Текст с черной обводкой (Stroke).
 * Используется для имени героя и количества юнитов.
 */
@Composable
fun OutlinedText(
    text: String,
    fontSize: TextUnit = 14.sp, // <-- РАЗМЕР ПО УМОЛЧАНИЮ
    textColor: Color = Color.White,
    strokeColor: Color = Color.Black,
    strokeWidth: Float = 5f // <-- ТОЛЩИНА ОБВОДКИ
) {
    Box(contentAlignment = Alignment.Center) {
        // Слой обводки (рисуется снизу)
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            style = TextStyle.Default.copy(
                drawStyle = Stroke(
                    miter = 10f,
                    width = strokeWidth,
                    join = StrokeJoin.Round
                )
            ),
            color = strokeColor
        )
        // Слой основного текста (рисуется поверх)
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}