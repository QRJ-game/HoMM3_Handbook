package com.example.homm3reference.data

import kotlin.text.get

object JSON_Mapper {
    fun parseArmy(armyString: String): List<Pair<String, String>> {
        val result = mutableListOf<Pair<String, String>>()
        if (armyString.isBlank()) return result

        val lines = armyString.split("\n")
        val regex = Regex("""^(\d+(?:-\d+)?)\s+(.+)$""")

        for (line in lines) {
            val match = regex.find(line.trim())
            if (match != null) {
                val (count, rawName) = match.destructured
                val cleanName = rawName.trim().lowercase()
                val imageRes = creatureMap[cleanName] ?: "creature_peasant"
                result.add(imageRes to count)
            }
        }
        return result
    }
    private val creatureMap = mapOf(
        // Замок
        "копейщиков" to "creature_pikeman", "копейщика" to "creature_pikeman",
        "арбалетчиков" to "creature_archer", "арбалетчика" to "creature_archer",
        "грифонов" to "creature_griffin", "грифона" to "creature_griffin",
        "мечников" to "creature_swordsman", "мечника" to "creature_swordsman",
        "монахов" to "creature_monk", "монаха" to "creature_monk",
        "кавалеристов" to "creature_cavalier", "кавалерист" to "creature_cavalier",

        // Оплот
        "кентавров" to "creature_centaur", "кентавра" to "creature_centaur",
        "гномов" to "creature_dwarf", "гнома" to "creature_dwarf",
        "эльфов" to "creature_wood_elf", "эльфа" to "creature_wood_elf",
        "пегасов" to "creature_pegasus", "пегаса" to "creature_pegasus",
        "дендроидов" to "creature_dendroid_guard", "дендроида" to "creature_dendroid_guard",
        "единорогов" to "creature_unicorn", "единорога" to "creature_unicorn",

        // Башня
        "гремлинов" to "creature_gremlin", "гремлина" to "creature_gremlin",
        "каменных горгулий" to "creature_stone_gargoyle", "каменных горгулии" to "creature_stone_gargoyle",
        "каменных големов" to "creature_stone_golem", "каменных голема" to "creature_stone_golem",
        "магов" to "creature_mage", "мага" to "creature_mage",
        "джиннов" to "creature_genie", "джинна" to "creature_genie",
        "наг" to "creature_naga", "наги" to "creature_naga",

        // Инферно
        "бесов" to "creature_imp", "беса" to "creature_imp",
        "гогов" to "creature_gog", "гога" to "creature_gog",
        "адских гончих" to "creature_hell_hound",
        "демонов" to "creature_demon",  "демона" to "creature_demon",
        "отродий пропасти" to "creature_pit_fiend", "отродия пропасти" to "creature_pit_lord",
        "ифритов" to "creature_efreet", "ифрита" to "creature_efreet",

        // Некрополис
        "скелетов" to "creature_skeleton", "скелета" to "creature_skeleton",
        "ходячих мертвецов" to "creature_walking_dead",  "ходячих мертвеца" to "creature_walking_dead",
        "духов" to "creature_wight", "духа" to "creature_wight",
        "вампиров" to "creature_vampire", "вампира" to "creature_vampire",
        "личей" to "creature_lich", "лича" to "creature_lich",
        "рыцарей ужаса" to "creature_black_knight", "рыцаря ужаса" to "creature_black_knight",

        // Темница
        "троглодитов" to "creature_troglodyte", "троглодита" to "creature_troglodyte",
        "гарпий" to "creature_harpy", "гарпии" to "creature_harpy",
        "бехолдеров" to "creature_beholder", "бехолдера" to "creature_beholder",
        "медуз" to "creature_medusa", "медузы" to "creature_medusa",
        "минотавров" to "creature_minotaur", "минотавров" to "creature_minotaur",

        // Цитадель
        "гоблинов" to "creature_goblin", "гоблина" to "creature_goblin",
        "наездников на волках" to "creature_wolf_rider", "наездника на волках" to "creature_wolf_rider",
        "орков" to "creature_orc", "орка" to "creature_orc",
        "огров" to "creature_ogre", "огра" to "creature_ogre",
        "птиц рух" to "creature_roc", "птицы рух" to "creature_roc",
        "циклопов" to "creature_cyclops", "циклопа" to "creature_cyclops",
        "чудищ" to "creature_behemoth", "чудища" to "creature_behemoth",

        // Крепость
        "гноллов" to "creature_gnoll", "гнолла" to "creature_gnoll",
        "ящеров" to "creature_lizardman", "ящера" to "creature_lizardman",
        "змиев" to "creature_serpent_fly", "змия" to "creature_serpent_fly",
        "василисков" to "creature_basilisk", "василиска" to "creature_basilisk",
        "горгон" to "creature_gorgon", "горгоны" to "creature_gorgon",
        "виверн" to "creature_wyvern", "виверны" to "creature_wyvern",

        // Сопряжение
        "пикси" to "creature_pixie",
        "воздушных элементалей" to "creature_air_elemental", "воздушных элементаля" to "creature_air_elemental",
        "водных элементалей" to "creature_water_elemental", "водных элементаля" to "creature_water_elemental",
        "огненных элементалей" to "creature_fire_elemental", "огненных элементаля" to "creature_fire_elemental",
        "земляных элементалей" to "creature_earth_elemental", "земляных элементаля" to "creature_earth_elemental",
        "пси-элементалей" to "creature_psychic_elemental", "пси-элементаля" to "creature_psychic_elemental",

        // Причал
        "нимф" to "creature_nymph", "нимфы" to "creature_nymph",
        "матросов" to "creature_crew_mate", "матроса" to "creature_crew_mate",
        "пиратов" to "creature_pirate", "пирата" to "creature_pirate",
        "корсаров" to "creature_corsair", "корсара" to "creature_corsair",
        "морских волков" to "creature_sea_dog", "морских волка" to "creature_sea_dog",
        "духов океана" to "creature_stormbird", "духа океана" to "creature_stormbird",
        "жриц моря" to "creature_sea_witch", "жрицы моря" to "creature_sea_witch",
        "никсов" to "creature_nix", "никса" to "creature_nix",


        // Фабрика
        "полуросликов" to "creature_halfling", "полурослика" to "creature_halfling",
        "механиков" to "creature_mechanic", "механиков" to "creature_mechanic",
        "броненосцев" to "creature_armadillo",  "броненосца" to "creature_armadillo",
        "автоматонов" to "creature_automaton",  "автоматона" to "creature_automaton",
        "песчаных червей" to "creature_sandworm",  "песчаных червя" to "creature_sandworm",
        "стрелков" to "creature_gunslinger", "стрелка" to "creature_gunslinger",

        // Нейтралы
        "снайпер" to "creature_sharpshooter","снайперов" to "creature_sharpshooter", "снайпера" to "creature_sharpshooter",
        "чародей" to "creature_enchanter","чародеев" to "creature_enchanter", "чародея" to "creature_enchanter",
        // Машины
        "баллиста" to "creature_ballista",
        "палатка" to "creature_first_aid_tent",
        "пушка" to "creature_cannon",
    )

    fun getSkillIcons(skillsString: String): List<String> {
        return skillsString.split(",") // Разделяем по запятой
            .map { it.trim() } // Убираем пробелы
            // .map { rawName ->
            // Убираем уровни навыков в скобках, например "Мудрость(продвинутый)" -> "Мудрость"
            // rawName.substringBefore("(").trim()
            // }
            .mapNotNull { skillName ->
                skillImageMap[skillName] // Ищем в карте
            }
    }
    private val skillImageMap = mapOf(
        "Артиллерия" to "basic_artillery",
        "Артиллерия(продвинутый)" to "advanced_artillery",
        "Баллистика" to "basic_ballistics",
        "Баллистика(продвинутый)" to "advanced_ballistics",
        "Волшебство" to "basic_sorcery",
        "Волшебство(продвинутый)" to "advanced_sorcery",
        "Грамотность" to "basic_scholar",
        "Грамотность(продвинутый)" to "advanced_scholar",
        "Дипломатия" to "basic_diplomacy",
        "Дипломатия(продвинутый)" to "advanced_diplomacy",
        "Доспехи" to "basic_armorer",
        "Доспехи(продвинутый)" to "advanced_armorer",
        "Интеллект" to "basic_intelligence",
        "Интеллект(продвинутый)" to "advanced_intelligence",
        "Лидерство" to "basic_leadership",
        "Лидерство(продвинутый)" to "advanced_leadership",
        "Логистика" to "basic_logistics",
        "Логистика(продвинутый)" to "advanced_logistics",
        "Магия Воды" to "basic_water_magic",
        "Магия Воды(продвинутый)" to "advanced_water_magic",
        "Магия Воздуха" to "basic_air_magic",
        "Магия Воздуха(продвинутый)" to "advanced_air_magic",
        "Магия Земли" to "basic_earth_magic",
        "Магия Земли(продвинутый)" to "advanced_earth_magic",
        "Магия Огня" to "basic_fire_magic",
        "Магия Огня(продвинутый)" to "advanced_fire_magic",
        "Магия Огня(эксперт)" to "expert_fire_magic",
        "Мистицизм" to "basic_mysticism",
        "Мистицизм(продвинутый)" to "advanced_mysticism",
        "Мудрость" to "basic_wisdom",
        "Мудрость(продвинутый)" to "advanced_wisdom",
        "Навигация" to "basic_navigation",
        "Навигация(продвинутый)" to "advanced_navigation",
        "Нападение" to "basic_offense",
        "Нападение(продвинутый)" to "advanced_offense",
        "Некромантия" to "basic_necromancy",
        "Некромантия(продвинутый)" to "advanced_necromancy",
        "Обучаемость" to "basic_learning",
        "Обучаемость(продвинутый)" to "advanced_learning",
        "Обучение" to "basic_learning",
        "Обучение(продвинутый)" to "advanced_learning",
        "Орлиный глаз" to "basic_eagle_eye",
        "Орлиный глаз(продвинутый)" to "advanced_eagle_eye",
        "Первая помощь" to "basic_first_aid",
        "Первая помощь(продвинутый)" to "advanced_first_aid",
        "Поиск пути" to "basic_pathfinding",
        "Поиск пути(продвинутый)" to "advanced_pathfinding",
        "Поместья" to "basic_estates",
        "Поместье(продвинутый)" to "advanced_estates",
        "Помехи" to "basic_interference",
        "Помехи(продвинутый)" to "advanced_interference",
        "Разведка" to "basic_scouting",
        "Разведка(продвинутый)" to "advanced_scouting",
        "Сопротивление" to "basic_resistance",
        "Сопротивление(продвинутый)" to "advanced_resistance",
        "Стрельба" to "basic_archery",
        "Стрельба(продвинутый)" to "advanced_archery",
        "Тактика" to "basic_tactics",
        "Тактика(продвинутый)" to "advanced_tactics",
        "Удача" to "basic_luck",
        "Удача(продвинутый)" to "advanced_luck"
    )
}