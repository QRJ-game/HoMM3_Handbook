package com.example.homm3reference.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
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
import com.example.homm3reference.data.JSON_Mapper
import com.example.homm3reference.ui.theme.HommGlassBackground
import com.example.homm3reference.ui.theme.HommGold

// Единые константы стиля
private val HommShape = RoundedCornerShape(8.dp)
private val HommBorder = BorderStroke(2.dp, HommGold)

@SuppressLint("DiscouragedApi")
@Composable
fun AppBackground(
    content: @Composable BoxScope.() -> Unit
) {
    val context = LocalContext.current
    val bgId = remember(Unit) {
        context.resources.getIdentifier("main_background", "drawable", context.packageName)
    }

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
                colorFilter = ColorFilter.colorMatrix(ColorMatrix(colorMatrix))
            )
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.2f)))
        } else {
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))
        }
        content()
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun TownCard(townName: String, onClick: () -> Unit) {
    val context = LocalContext.current
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
        "Кронверк" -> "town_kronverk"
        "Нейтралы" -> "town_neutral"
        "Боевые машины" -> "creature_ballista"
        else -> "icon_image"
    }

    val resId = remember(imageResName) {
        context.resources.getIdentifier(imageResName, "drawable", context.packageName)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f)
            .clickable { onClick() },
        shape = HommShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = HommBorder
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (resId != 0) {
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
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

            Text(
                text = townName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = HommGold,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun HeroImage(
    imageName: String,
    modifier: Modifier = Modifier,
    width: Dp = 58.dp,
    height: Dp = 64.dp,
    contentScale: ContentScale = ContentScale.Crop,
    borderWidth: Dp = 2.dp,
    borderColor: Color = HommGold
) {
    val context = LocalContext.current
    val resId = remember(imageName) {
        context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }

    val finalModifier = modifier
        .width(width)
        .height(height)
        .clip(RoundedCornerShape(8.dp))
        .border(borderWidth, borderColor, RoundedCornerShape(8.dp))

    if (resId != 0) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            modifier = finalModifier,
            contentScale = contentScale
        )
    } else {
        Box(
            modifier = finalModifier.background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text("?", color = Color.White)
        }
    }
}

@Composable
fun StatItem(label: String, icon: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 24.sp)
        Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color.White)
        Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.9f))
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, color = HommGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = value, fontWeight = FontWeight.Medium, color = Color.White, fontSize = 16.sp)
    }
}

@Composable
fun ArmyVisuals(armyString: String, onCreatureClick: (String) -> Unit) {
    val armyList = remember(armyString) { JSON_Mapper.parseArmy(armyString) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        armyList.forEach { (imageRes, count) ->
            ArmySlot(imageRes, count, onClick = { onCreatureClick(imageRes) })
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun ArmySlot(imageRes: String, count: String, onClick: () -> Unit) {
    val context = LocalContext.current
    val resId = remember(imageRes) {
        context.resources.getIdentifier(imageRes, "drawable", context.packageName)
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.padding(4.dp).clickable { onClick() }
    ) {
        Card(
            modifier = Modifier.width(100.dp).height(130.dp),
            shape = HommShape,
            colors = CardDefaults.cardColors(containerColor = HommGlassBackground),
            elevation = CardDefaults.cardElevation(4.dp),
            border = HommBorder
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                if (resId != 0) {
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().offset(y = (-15).dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
        Box(modifier = Modifier.offset(y = 0.dp)) {
            OutlinedText(text = count, fontSize = 14.sp)
        }
    }
}

@Composable
fun OutlinedText(
    text: String,
    fontSize: TextUnit = 14.sp,
    textColor: Color = Color.White,
    strokeColor: Color = Color.Black,
    strokeWidth: Float = 5f
) {
    Box(contentAlignment = Alignment.Center) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            style = TextStyle.Default.copy(drawStyle = Stroke(miter = 10f, width = strokeWidth, join = StrokeJoin.Round)),
            color = strokeColor
        )
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun AppSearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "Поиск..."
) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = modifier
            .fillMaxWidth()
            .border(2.dp, HommGold, HommShape)
            .clip(HommShape),
        placeholder = { Text(placeholderText, color = Color.Gray) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = HommGlassBackground,
            unfocusedContainerColor = HommGlassBackground,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = HommGold,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        leadingIcon = { Text("🔍", fontSize = 18.sp, color = HommGold) }
    )
}

// Добавьте этот код в конец файла или в удобное место
@SuppressLint("DiscouragedApi")
@Composable
fun HommListCard(
    text: String,
    imageRes: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageId = remember(imageRes) {
        context.resources.getIdentifier(imageRes, "drawable", context.packageName)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = HommGlassBackground),
        border = HommBorder, // Используем золотую рамку для единообразия меню
        shape = HommShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp) // Отступы как в карточке героя
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка в стиле портрета героя
            if (imageId != 0) {
                Image(
                    painter = painterResource(id = imageId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(-1.dp, HommGold, RoundedCornerShape(8.dp)), // Золотая рамка вокруг иконки
                    contentScale = ContentScale.Crop
                )
            } else {
                // Заглушка
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray.copy(alpha = 0.6f))
                        .border(-1.dp, HommGold, RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = text,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = HommGold
            )
        }
    }
}