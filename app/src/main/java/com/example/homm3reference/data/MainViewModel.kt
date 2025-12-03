package com.example.homm3reference

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // Сохраняем состояние "выключен ли звук пользователем"
    // Используем mutableStateOf, чтобы Compose мог реагировать на изменения
    var isMuted by mutableStateOf(false)
        private set

    private var mediaPlayer: MediaPlayer? = null
    private val prefs = application.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    init {
        // 1. Загружаем сохраненную настройку при старте
        isMuted = prefs.getBoolean("is_muted", false)

        // 2. Инициализируем плеер
        try {
            val resId = application.resources.getIdentifier("heroes_music", "raw", application.packageName)
            if (resId != 0) {
                mediaPlayer = MediaPlayer.create(application, resId).apply {
                    isLooping = true
                    setVolume(1.0f, 1.0f)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 3. Если звук не был выключен, запускаем музыку
        if (!isMuted) {
            mediaPlayer?.start()
        }
    }

    // Метод для переключения звука (вызывается из UI)
    fun toggleMute() {
        isMuted = !isMuted
        // Сохраняем выбор в память телефона
        prefs.edit().putBoolean("is_muted", isMuted).apply()

        if (isMuted) {
            if (mediaPlayer?.isPlaying == true) mediaPlayer?.pause()
        } else {
            mediaPlayer?.start()
        }
    }

    // Вызывается из Activity onResume (вернулись в приложение)
    fun onResume() {
        // Если пользователь НЕ выключал звук, и плеер не играет -> запускаем
        if (!isMuted && mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    // Вызывается из Activity onPause (свернули приложение)
    fun onPause() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    // Очистка ресурсов при полном закрытии ViewModel (обычно при полном закрытии приложения)
    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}