package com.example.homm3reference.data

import com.google.gson.annotations.SerializedName

data class Artifact(
    val id: String,
    val name: String,
    val imageRes: String,
    @SerializedName("class") val classType: String, // 'class' - зарезервированное слово
    val slot: String,
    val goldCost: Int,
    val properties: String,
    val set: String?,
    val group: String
)