package com.example.homm3reference.data

data class SecondarySkill(
    val id: String,
    val name: String,
    val imageRes: String,
    val basic: String,
    val advanced: String,
    val expert: String
)

object SecondarySkillsRepo {
    val skills = listOf(
        SecondarySkill(
            id = "pathfinding",
            name = "Поиск Пути",
            imageRes = "secondary_pathfinding",
            basic = "Герой имеет штрафы при движении по камням, песку, снегу и болоту (-25% штрафа).",
            advanced = "Герой не имеет штрафа при движении по камням. -25% штрафа при движении по песку, снегу и болоту.",
            expert = "Герой не имеет штрафа при движении по камням, песку, снегу и болоту."
        ),
        SecondarySkill(
            id = "archery",
            name = "Стрельба",
            imageRes = "secondary_archery",
            basic = "+10% к наносимому существами урону при стрельбе. Действует на Баллисту.",
            advanced = "+25% к наносимому урону при стрельбе. Действует на Баллисту.",
            expert = "+50% к наносимому урону при стрельбе. Действует на Баллисту, Пушку и Стрелковые башни."
        ),
        SecondarySkill(
            id = "logistics",
            name = "Логистика",
            imageRes = "secondary_logistics",
            basic = "+10% к максимальному запасу хода героя по суше.",
            advanced = "+20% к максимальному запасу хода героя по суше.",
            expert = "+30% к максимальному запасу хода героя по суше."
        ),
        SecondarySkill(
            id = "scouting",
            name = "Разведка",
            imageRes = "secondary_scouting",
            basic = "Герой открывает карту вокруг себя в радиусе 5 клеток (диаметр 11).",
            advanced = "Герой открывает карту вокруг себя в радиусе 7 клеток (диаметр 15).",
            expert = "Герой открывает карту вокруг себя в радиусе 9 клеток (диаметр 19)."
        ),
        SecondarySkill(
            id = "diplomacy",
            name = "Дипломатия",
            imageRes = "secondary_diplomacy",
            basic = "10% шанс присоединения нейтральных существ. -20% цены при откупе. Библиотека требует 8 уровень.",
            advanced = "20% шанс присоединения. -40% цены при откупе. Библиотека требует 6 уровень.",
            expert = "30% шанс присоединения. -60% цены при откупе. Библиотека требует 4 уровень."
        ),
        SecondarySkill(
            id = "navigation",
            name = "Навигация",
            imageRes = "secondary_navigation",
            basic = "+50% к запасу хода героя на воде.",
            advanced = "+100% к запасу хода героя на воде.",
            expert = "+150% к запасу хода героя на воде."
        ),
        SecondarySkill(
            id = "leadership",
            name = "Лидерство",
            imageRes = "secondary_leadership",
            basic = "+1 к боевому духу.",
            advanced = "+2 к боевому духу.",
            expert = "+3 к боевому духу."
        ),
        SecondarySkill(
            id = "wisdom",
            name = "Мудрость",
            imageRes = "secondary_wisdom",
            basic = "Герой может изучать заклинания 3 уровня.",
            advanced = "Герой может изучать заклинания 4 уровня.",
            expert = "Герой может изучать заклинания 5 уровня."
        ),
        SecondarySkill(
            id = "mysticism",
            name = "Мистицизм",
            imageRes = "secondary_mysticism",
            basic = "Герой восстанавливает по 2 маны в день.",
            advanced = "Герой восстанавливает по 3 маны в день.",
            expert = "Герой восстанавливает по 4 маны в день."
        ),
        SecondarySkill(
            id = "luck",
            name = "Удача",
            imageRes = "secondary_luck",
            basic = "+1 к удаче.",
            advanced = "+2 к удаче.",
            expert = "+3 к удаче."
        ),
        SecondarySkill(
            id = "ballistics",
            name = "Баллистика",
            imageRes = "secondary_ballistics",
            basic = "Контроль над Катапультой. Увеличенный шанс попадания.",
            advanced = "То же + Двойной выстрел.",
            expert = "То же + Максимальный шанс попадания и урон по укреплениям."
        ),
        SecondarySkill(
            id = "eagle_eye",
            name = "Орлиный глаз",
            imageRes = "secondary_eagle_eye",
            basic = "40% шанс изучить заклинание 1-2 уровня, использованное врагом.",
            advanced = "50% шанс изучить заклинание 1-3 уровня, использованное врагом.",
            expert = "60% шанс изучить заклинание 1-4 уровня, использованное врагом."
        ),
        SecondarySkill(
            id = "necromancy",
            name = "Некромантия",
            imageRes = "secondary_necromancy",
            basic = "10% убитых врагов воскрешаются в виде Скелетов.",
            advanced = "20% убитых врагов воскрешаются в виде Скелетов.",
            expert = "30% убитых врагов воскрешаются в виде Скелетов."
        ),
        SecondarySkill(
            id = "estates",
            name = "Поместья",
            imageRes = "secondary_estates",
            basic = "+125 золота в день.",
            advanced = "+250 золота в день.",
            expert = "+500 золота в день."
        ),
        SecondarySkill(
            id = "fire_magic",
            name = "Магия Огня",
            imageRes = "secondary_fire_magic",
            basic = "Стоимость заклинаний огня снижена. Базовая эффективность.",
            advanced = "Продвинутая эффективность заклинаний огня.",
            expert = "Экспертная эффективность заклинаний огня (массовые заклинания)."
        ),
        SecondarySkill(
            id = "air_magic",
            name = "Магия Воздуха",
            imageRes = "secondary_air_magic",
            basic = "Стоимость заклинаний воздуха снижена. Базовая эффективность.",
            advanced = "Продвинутая эффективность заклинаний воздуха.",
            expert = "Экспертная эффективность заклинаний воздуха (массовые заклинания)."
        ),
        SecondarySkill(
            id = "earth_magic",
            name = "Магия Земли",
            imageRes = "secondary_earth_magic",
            basic = "Стоимость заклинаний земли снижена. Базовая эффективность.",
            advanced = "Продвинутая эффективность заклинаний земли.",
            expert = "Экспертная эффективность заклинаний земли (массовые заклинания)."
        ),
        SecondarySkill(
            id = "water_magic",
            name = "Магия Воды",
            imageRes = "secondary_water_magic",
            basic = "Стоимость заклинаний воды снижена. Базовая эффективность.",
            advanced = "Продвинутая эффективность заклинаний воды.",
            expert = "Экспертная эффективность заклинаний воды (массовые заклинания)."
        ),
        SecondarySkill(
            id = "tactics",
            name = "Тактика",
            imageRes = "secondary_tactics",
            basic = "Расстановка войск в области 3 рядов клеток.",
            advanced = "Расстановка войск в области 5 рядов клеток.",
            expert = "Расстановка войск в области 7 рядов клеток."
        ),
        SecondarySkill(
            id = "artillery",
            name = "Артиллерия",
            imageRes = "secondary_artillery",
            basic = "Контроль над Баллистой. 50% шанс двойного урона.",
            advanced = "75% шанс двойного урона + Двойной выстрел Баллисты.",
            expert = "Двойной урон + Двойной выстрел Баллисты. Контроль Стрелковых башен."
        ),
        SecondarySkill(
            id = "learning",
            name = "Обучаемость",
            imageRes = "secondary_learning",
            basic = "+5% к получаемому опыту.",
            advanced = "+10% к получаемому опыту.",
            expert = "+15% к получаемому опыту."
        ),
        SecondarySkill(
            id = "offense",
            name = "Нападение",
            imageRes = "secondary_offense",
            basic = "+10% к урону в ближнем бою.",
            advanced = "+20% к урону в ближнем бою.",
            expert = "+30% к урону в ближнем бою."
        ),
        SecondarySkill(
            id = "armorer",
            name = "Доспехи",
            imageRes = "secondary_armorer",
            basic = "-5% получаемого урона.",
            advanced = "-10% получаемого урона.",
            expert = "-15% получаемого урона."
        ),
        SecondarySkill(
            id = "intelligence",
            name = "Интеллект",
            imageRes = "secondary_intelligence",
            basic = "+25% к максимуму маны.",
            advanced = "+50% к максимуму маны.",
            expert = "+100% к максимуму маны."
        ),
        SecondarySkill(
            id = "sorcery",
            name = "Волшебство",
            imageRes = "secondary_sorcery",
            basic = "+5% к урону ударных заклинаний.",
            advanced = "+10% к урону ударных заклинаний.",
            expert = "+15% к урону ударных заклинаний."
        ),
        SecondarySkill(
            id = "resistance",
            name = "Сопротивление",
            imageRes = "secondary_resistance",
            basic = "5% шанс отразить магию.",
            advanced = "10% шанс отразить магию.",
            expert = "20% шанс отразить магию."
        ),
        SecondarySkill(
            id = "first_aid",
            name = "Первая помощь",
            imageRes = "secondary_first_aid",
            basic = "Контроль Палатки. Лечение до 50 ед.",
            advanced = "Лечение до 75 ед.",
            expert = "Лечение до 100 ед."
        ),
        SecondarySkill(
            id = "interference",
            name = "Помехи",
            imageRes = "secondary_interference",
            basic = "Сила магии врага снижается на 10%.",
            advanced = "Сила магии врага снижается на 20%.",
            expert = "Сила магии врага снижается на 30%."
        ),
        SecondarySkill(
            id = "scholar",
            name = "Грамотность",
            imageRes = "secondary_scholar",
            basic = "Обмен заклинаниями 2 уровня между героями.",
            advanced = "Обмен заклинаниями 3 уровня между героями.",
            expert = "Обмен заклинаниями 4 уровня между героями."
        )
    )
}