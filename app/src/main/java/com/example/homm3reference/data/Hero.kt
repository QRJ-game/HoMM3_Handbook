package com.example.homm3reference.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "heroes")
data class Hero(
    @PrimaryKey val id: String,
    val name: String,
    val town: String,
    val heroClass: String,
    val classType: String,
    val specialty: String,
    val skills: String,
    val spell: String?,
    val army: String,
    val imageRes: String,
    val backgroundColor: String? = null,
    val specialtyIcon: String? = null,
    @SerializedName("specialtyDescription")
    val specialtyDescription: String? = null,
    // Добавляем новое поле для биографии
    val biography: String? = null
)