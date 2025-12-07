package com.example.homm3reference.data

data class Spell(
    val id: String,
    val imageRes: String,
    val name: String,
    val level: Int,
    val school: String,

    // Цены маны
    val manaCostNone: Int,
    val manaCostBasic: Int,
    val manaCostAdvanced: Int,
    val manaCostExpert: Int,

    // Описание действий
    val descriptionNone: String,
    val descriptionBasic: String,
    val descriptionAdvanced: String,
    val descriptionExpert: String,

    // Цвет подложки (hex код, например "#DC143C"). Может быть null.
    val backgroundColor: String? = null
)