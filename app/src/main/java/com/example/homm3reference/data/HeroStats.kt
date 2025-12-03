package com.example.homm3reference.data

object HeroBaseStats {
    data class Stats(
        val attack: Int,
        val defense: Int,
        val power: Int,
        val knowledge: Int
    )

    private val classMap = mapOf(
        "Рыцарь" to Stats(2, 2, 1, 1),
        "Священник" to Stats(1, 0, 2, 2),
        "Рейнджер" to Stats(1, 3, 1, 1),
        "Друид" to Stats(0, 2, 1, 2),
        "Алхимик" to Stats(1, 1, 2, 2),
        "Маг" to Stats(0, 0, 2, 3),
        "Демон" to Stats(2, 2, 1, 1),
        "Еретик" to Stats(1, 1, 2, 1),
        "Рыцарь смерти" to Stats(1, 2, 2, 1),
        "Некромант" to Stats(1, 0, 2, 2),
        "Лорд" to Stats(2, 2, 1, 1),
        "Чернокнижник" to Stats(0, 0, 3, 2),
        "Варвар" to Stats(4, 0, 1, 1),
        "Боевой маг" to Stats(2, 1, 1, 1),
        "Повелитель зверей" to Stats(0, 4, 1, 1),
        "Ведьма" to Stats(0, 1, 2, 2),
        "Пси-элементаль" to Stats(3, 1, 1, 1),
        "Элементалист" to Stats(0, 0, 3, 3),
        "Капитан" to Stats(3, 0, 2, 1),
        "Навигатор" to Stats(2, 0, 1, 2),
        "Наёмник" to Stats(3, 1, 1, 1),
        "Изобретатель" to Stats(0, 1, 2, 2)
    )

    fun getStats(className: String): Stats {
        return classMap[className] ?: Stats(0, 0, 0, 0)
    }
}