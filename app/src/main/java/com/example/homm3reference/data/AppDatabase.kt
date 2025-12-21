package com.example.homm3reference.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface HeroDao {
    @Query("SELECT * FROM heroes ORDER BY name ASC")
    fun getAllHeroes(): Flow<List<Hero>>

    @Query("SELECT * FROM heroes WHERE name LIKE '%' || :searchQuery || '%' OR heroClass LIKE '%' || :searchQuery || '%' OR town LIKE '%' || :searchQuery || '%'")
    fun searchHeroes(searchQuery: String): Flow<List<Hero>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(heroes: List<Hero>)

    @Query("SELECT COUNT(*) FROM heroes")
    suspend fun getCount(): Int
}

@Dao
interface CreatureDao {
    @Query("SELECT * FROM creatures ORDER BY level ASC, isUpgraded ASC")
    fun getAllCreatures(): Flow<List<Creature>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(creatures: List<Creature>)

    @Query("SELECT COUNT(*) FROM creatures")
    suspend fun getCount(): Int
}

@Database(entities = [Hero::class, Creature::class], version = 8) //НЕ ЗАБЫВАТЬ МЕНЯТЬ!!!
abstract class AppDatabase : RoomDatabase() {
    abstract fun heroDao(): HeroDao
    abstract fun creatureDao(): CreatureDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "homm3_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}