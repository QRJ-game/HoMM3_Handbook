package com.example.homm3reference.data

// Единый Singleton для данных, которые не хранятся в БД
object GameData {
    var secondarySkills: List<SecondarySkill> = emptyList()
    var heroClassStats: List<HeroClassStat> = emptyList()

    // Вспомогательный метод для быстрого поиска статов
    fun getStatsForClass(className: String): HeroClassStat {
        return heroClassStats.find { it.className == className }
            ?: HeroClassStat(className, 0, 0, 0, 0)
    }
}