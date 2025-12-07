package com.example.homm3reference.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object DataLoader {
    // Загрузка героев в БД
    suspend fun loadHeroes(context: Context, dao: HeroDao) {
        withContext(Dispatchers.IO) {
            try {
                // Читаем файл
                val jsonString = context.assets.open("heroes_data.json").bufferedReader().use { it.readText() }

                // Парсим
                val listType = object : TypeToken<List<Hero>>() {}.type
                val heroes: List<Hero> = Gson().fromJson(jsonString, listType)
                dao.insertAll(heroes)
            } catch (e: Exception) {
                Log.e("HOMM_DEBUG", "Ошибка загрузки: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    // Загрузка существ в БД
    suspend fun loadCreatures(context: Context, dao: CreatureDao) {
        withContext(Dispatchers.IO) {
            try {
                val jsonString = context.assets.open("creatures_data.json").bufferedReader().use { it.readText() }
                val listType = object : TypeToken<List<Creature>>() {}.type
                val creatures: List<Creature> = Gson().fromJson(jsonString, listType)
                dao.insertAll(creatures)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    // Загрузка вторичных навыков
    suspend fun loadSecondarySkills(context: Context) {
        withContext(Dispatchers.IO) {
            val jsonString = loadJsonFromAsset(context, "secondary_skills.json")
            if (jsonString != null) {
                val type = object : TypeToken<List<SecondarySkill>>() {}.type
                val skills: List<SecondarySkill> = Gson().fromJson(jsonString, type)
                GameData.secondarySkills = skills
            }
        }
    }

    // Загрузка классов
    suspend fun loadHeroClasses(context: Context) {
        withContext(Dispatchers.IO) {
            val jsonString = loadJsonFromAsset(context, "hero_classes.json")
            if (jsonString != null) {
                val type = object : TypeToken<List<HeroClassStat>>() {}.type
                val stats: List<HeroClassStat> = Gson().fromJson(jsonString, type)
                GameData.heroClassStats = stats
            }
        }
    }

    // Загрузка заклинаний
    suspend fun loadSpells(context: Context) {
        withContext(Dispatchers.IO) {
            val jsonString = loadJsonFromAsset(context, "spells_data.json")
            if (jsonString != null) {
                val type = object : TypeToken<List<Spell>>() {}.type
                val spells: List<Spell> = Gson().fromJson(jsonString, type)
                GameData.spells = spells
            }
        }
    }

    private fun loadJsonFromAsset(context: Context, fileName: String): String? {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            null
        }
    }
}