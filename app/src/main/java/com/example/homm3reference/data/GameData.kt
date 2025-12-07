package com.example.homm3reference.data

import android.util.Log

// Единый Singleton для данных
object GameData {
    var secondarySkills: List<SecondarySkill> = emptyList()
    var heroClassStats: List<HeroClassStat> = emptyList()
    var spells: List<Spell> = emptyList()
    // Нужно добавить ссылку на существ, чтобы искать их иконки
    var creatures: List<Creature> = emptyList()

    fun getStatsForClass(className: String): HeroClassStat {
        return heroClassStats.find { it.className == className }
            ?: HeroClassStat(className, 0, 0, 0, 0)
    }

    // Новая функция для поиска иконки специализации
    fun getSpecialtyIcon(specialtyName: String): String? {
        // 1. Ищем в существах (по name)
        // Некоторые специализации называются как существа (например "Грифоны"),
        // а некоторые имеют специфичные названия ("Скорость" у Сэра Мюллиха).

        // Попытка точного совпадения с именем существа
        val creature = creatures.find { it.name.equals(specialtyName, ignoreCase = true) }
        if (creature != null) return creature.imageRes

        // Попытка найти существо, если специализация содержит его имя (для множественного числа)
        // Это простая эвристика, может потребоваться уточнение.
        // Но чаще всего в JSON имена совпадают или очень похожи.

        // 2. Ищем в заклинаниях (по name)
        val spell = spells.find { it.name.equals(specialtyName, ignoreCase = true) }
        if (spell != null) return spell.imageRes

        // 3. Ищем во вторичных навыках (по name)
        val skill = secondarySkills.find { it.name.equals(specialtyName, ignoreCase = true) }
        if (skill != null) return "expert_${skill.id}" // Берем экспертную иконку

        // Специальные случаи (можно расширять)
        return when(specialtyName) {
            "Баллиста" -> "creature_ballista"
            "Пушка" -> "creature_cannon"
            "Палатка" -> "creature_first_aid_tent" // Если есть такая картинка
            // Добавьте другие специфичные маппинги, если нужно
            else -> null
        }
    }

    // Функция для проверки всех героев и вывода в лог
    fun checkMissingSpecialtyIcons(heroes: List<Hero>) {
        val tag = "SpecialtyCheck"
        Log.d(tag, "--- START CHECKING SPECIALTIES ---")
        var missingCount = 0
        heroes.forEach { hero ->
            val icon = getSpecialtyIcon(hero.specialty)
            if (icon == null) {
                Log.w(tag, "Missing icon for hero: ${hero.name}, Specialty: ${hero.specialty}")
                missingCount++
            }
        }
        Log.d(tag, "--- END CHECKING. Total missing: $missingCount ---")
    }
}