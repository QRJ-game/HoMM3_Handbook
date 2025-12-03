package com.example.homm3reference.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object DataLoader {
    suspend fun loadHeroes(context: Context, dao: HeroDao) {
        withContext(Dispatchers.IO) {
            try {
                val jsonString = context.assets.open("heroes_data.json").bufferedReader().use { it.readText() }
                val listType = object : TypeToken<List<Hero>>() {}.type
                val heroes: List<Hero> = Gson().fromJson(jsonString, listType)
                dao.insertAll(heroes)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

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

    // Вспомогательная функция (если её еще нет, или используйте существующую логику внутри loadHeroes)
    private fun loadJsonFromAsset(context: Context, fileName: String): String? {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            null
        }
    }
}