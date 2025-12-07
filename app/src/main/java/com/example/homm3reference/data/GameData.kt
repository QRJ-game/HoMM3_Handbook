package com.example.homm3reference.data

// Единый Singleton для данных, которые не хранятся в БД (или пока загружаются в память)
object GameData {
    var secondarySkills: List<SecondarySkill> = emptyList()
    var heroClassStats: List<HeroClassStat> = emptyList()
    var spells: List<Spell> = emptyList() // <-- Новое поле для заклинаний

    // Вспомогательный метод для быстрого поиска статов
    fun getStatsForClass(className: String): HeroClassStat {
        return heroClassStats.find { it.className == className }
            ?: HeroClassStat(className, 0, 0, 0, 0)
    }
}