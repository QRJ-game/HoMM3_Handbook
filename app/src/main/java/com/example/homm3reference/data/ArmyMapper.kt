package com.example.homm3reference.data

object ArmyMapper {
    fun parseArmy(armyString: String): List<Pair<String, String>> {
        val result = mutableListOf<Pair<String, String>>()
        if (armyString.isBlank()) return result

        val lines = armyString.split("\n")
        val regex = Regex("""^(\d+(?:-\d+)?)\s+(.+)$""")

        for (line in lines) {
            val match = regex.find(line.trim())
            if (match != null) {
                android.util.Log.e("ArmyMapper", "Не найдено существо: '$line.trim', заменено на крестьянина")
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
}