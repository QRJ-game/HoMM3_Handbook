package com.example.homm3reference.data

import android.util.Log

object GameData {
    var secondarySkills: List<SecondarySkill> = emptyList()
    var heroClassStats: List<HeroClassStat> = emptyList()
    var spells: List<Spell> = emptyList()
    var creatures: List<Creature> = emptyList()
    var artifacts: List<Artifact> = emptyList()
    var heroes: List<Hero> = emptyList()

    fun getStatsForClass(className: String): HeroClassStat {
        return heroClassStats.find { it.className == className }
            ?: HeroClassStat(className, 0, 0, 0, 0)
    }

    fun getSpecialtyIcon(specialtyName: String): String? {
        val creature = creatures.find { it.name.equals(specialtyName, ignoreCase = true) }
        if (creature != null) return creature.imageRes

        val spell = spells.find { it.name.equals(specialtyName, ignoreCase = true) }
        if (spell != null) return spell.imageRes

        val skill = secondarySkills.find { it.name.equals(specialtyName, ignoreCase = true) }
        if (skill != null) return "expert_${skill.id}"

        return when(specialtyName) {
            "Баллиста" -> "creature_ballista"
            "Пушка" -> "creature_cannon"
            "Палатка" -> "creature_first_aid_tent"
            else -> null
        }
    }

    fun checkMissingSpecialtyIcons(heroes: List<Hero>) {
        val tag = "SpecialtyCheck"
        Log.d(tag, "--- START CHECKING SPECIALTIES ---")
        var missingCount = 0
        heroes.forEach { hero ->
            // Исправлено: теперь результат используется (хотя бы для проверки)
            if (getSpecialtyIcon(hero.specialty) == null) {
                missingCount++
                Log.e(tag, hero.name)
            }
        }
        Log.d(tag, "--- END CHECKING. Total missing: $missingCount ---")
    }
}