package com.example.homm3reference.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
}