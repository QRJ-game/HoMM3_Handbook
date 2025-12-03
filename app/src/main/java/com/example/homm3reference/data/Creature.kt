package com.example.homm3reference.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "creatures")
data class Creature(
    @PrimaryKey val id: String,
    val name: String,
    val town: String,
    val level: Int,
    val isUpgraded: Boolean,
    val minDamage: Int,
    val maxDamage: Int,
    val attack: Int,
    val defense: Int,
    val health: Int,
    val speed: Int,
    val growth: Int,
    val goldCost: Int,
    val resourceCost: String?,
    val abilities: String,
    val aiValue: Int,
    val imageRes: String
)