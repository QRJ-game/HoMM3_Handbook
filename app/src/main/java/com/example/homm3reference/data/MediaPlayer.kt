package com.example.homm3reference.data // <--- ИСПРАВЛЕНО (добавлено .data)

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

import kotlin.random.Random

class MediaPlayer(application: Application) : AndroidViewModel(application) {

    var isMuted by mutableStateOf(false)
        private set

    private var mediaPlayer: MediaPlayer? = null
    private val prefs = application.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    // Список ID найденных музыкальных треков
    private val playlist = mutableListOf<Int>()
    // Индекс текущего трека
    private var currentTrackIndex = 0

    init {
        // 1. Загружаем настройку звука
        isMuted = prefs.getBoolean("is_muted", false)

        // 2. Ищем все доступные треки (heroes_music1, heroes_music2 и т.д.)
        loadMusicTracks(application)

        // 3. Если треки найдены, выбираем случайный и готовим плеер
        if (playlist.isNotEmpty()) {
            currentTrackIndex = Random.nextInt(playlist.size)
            initializePlayer(application)
        }
    }

    /**
     * Сканирует папку raw на наличие файлов heroes_music1, heroes_music2...
     * и добавляет их ID в плейлист.
     */
    private fun loadMusicTracks(context: Context) {
        var i = 1
        while (true) {
            // Пытаемся найти ресурс с именем heroes_music + номер
            val resId = context.resources.getIdentifier("heroes_music$i", "raw", context.packageName)
            if (resId != 0) {
                playlist.add(resId)
                i++
            } else {
                // Если файл с таким номером не найден, прерываем поиск
                break
            }
        }

        // (Опционально) Если нумерованных файлов нет, пробуем найти старый "heroes_music"
        if (playlist.isEmpty()) {
            val legacyId = context.resources.getIdentifier("heroes_music", "raw", context.packageName)
            if (legacyId != 0) {
                playlist.add(legacyId)
            }
        }
    }

    /**
     * Инициализирует плеер для текущего трека (currentTrackIndex)
     */
    private fun initializePlayer(context: Context) {
        // Освобождаем предыдущий плеер, если он был
        mediaPlayer?.release()

        if (playlist.isEmpty()) return

        try {
            val resId = playlist[currentTrackIndex]
            mediaPlayer = MediaPlayer.create(context, resId).apply {
                // Важно: выключаем зацикливание одного трека (isLooping = false),
                // чтобы сработал слушатель окончания (OnCompletionListener)
                isLooping = false
                setVolume(1.0f, 1.0f)

                // Слушатель окончания трека: запускает следующий
                setOnCompletionListener {
                    playNextTrack(context)
                }
            }

            // Если звук включен, сразу начинаем играть
            if (!isMuted) {
                mediaPlayer?.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Переключает индекс на следующий и перезапускает плеер
     */
    private fun playNextTrack(context: Context) {
        if (playlist.isEmpty()) return

        // Увеличиваем индекс на 1. Если дошли до конца (% playlist.size), сбрасываем в 0.
        currentTrackIndex = (currentTrackIndex + 1) % playlist.size

        initializePlayer(context)
    }

    // Метод для переключения звука (вызывается из UI)
    fun toggleMute() {
        isMuted = !isMuted
        prefs.edit().putBoolean("is_muted", isMuted).apply()

        if (isMuted) {
            if (mediaPlayer?.isPlaying == true) mediaPlayer?.pause()
        } else {
            // Если плеер не играет (например, был на паузе), запускаем
            // Если плеера нет (список был пуст), ничего не произойдет
            mediaPlayer?.start()
        }
    }

    fun onResume() {
        // Если не выключен звук и плеер есть, но не играет -> play
        if (!isMuted && mediaPlayer != null && mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    fun onPause() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}