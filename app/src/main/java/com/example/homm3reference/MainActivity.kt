package com.example.homm3reference

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import com.example.homm3reference.data.AppDatabase
import com.example.homm3reference.data.DataLoader
import com.example.homm3reference.data.MainViewModel // <--- Добавлен импорт ViewModel
import com.example.homm3reference.ui.navigation.AppRoot // <--- ИСПРАВЛЕН ИМПОРТ (добавлено .ui)
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    // Инициализируем ViewModel. Она переживет поворот экрана.
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val heroDao = database.heroDao()
        val creatureDao = database.creatureDao()

        lifecycleScope.launch {
            DataLoader.loadHeroes(applicationContext, heroDao)
            DataLoader.loadCreatures(applicationContext, creatureDao)
        }

        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFD4AF37),
                    background = Color(0xFF1E1E1E),
                    surface = Color(0xFF2D2D2D),
                    onSurface = Color.White
                )
            ) {
                // Берем состояние и функцию переключения прямо из ViewModel
                AppRoot(
                    heroDao = heroDao,
                    creatureDao = creatureDao,
                    isMuted = viewModel.isMuted,
                    onMuteToggle = { viewModel.toggleMute() }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Сообщаем ViewModel, что мы вернулись на экран
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        // Сообщаем ViewModel, что приложение свернуто
        if (!isChangingConfigurations) {
            viewModel.onPause()
        }
    }
}