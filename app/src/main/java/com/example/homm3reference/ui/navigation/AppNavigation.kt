package com.example.homm3reference.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import com.example.homm3reference.data.Creature
import com.example.homm3reference.data.CreatureDao
import com.example.homm3reference.data.Hero
import com.example.homm3reference.data.HeroDao
import com.example.homm3reference.data.SecondarySkill
import com.example.homm3reference.data.Spell // <-- Import
import com.example.homm3reference.ui.common.TownSelectionScreen
import com.example.homm3reference.ui.creatures.CreatureDetailScreen
import com.example.homm3reference.ui.creatures.CreatureListScreen
import com.example.homm3reference.ui.heroes.ClassSelectionScreen
import com.example.homm3reference.ui.heroes.HeroDetailScreen
import com.example.homm3reference.ui.heroes.HeroListScreen
import com.example.homm3reference.ui.magic.MagicSchoolSelectScreen // <-- Import
import com.example.homm3reference.ui.magic.SpellDetailScreen // <-- Import
import com.example.homm3reference.ui.magic.SpellListScreen // <-- Import
import com.example.homm3reference.ui.main_menu.MainMenuScreen
import com.example.homm3reference.ui.skills.SecondarySkillDetailScreen
import com.example.homm3reference.ui.skills.SecondarySkillsListScreen

enum class Screen {
    MainMenu,
    HeroTowns, HeroClasses, HeroList, HeroDetail,
    CreatureTowns, CreatureList, CreatureDetail,
    SkillsList, SkillDetail,
    MagicSchools, MagicList, MagicDetail // <--- Новые экраны для Магии
}

val TOWN_ORDER = listOf(
    "Замок", "Оплот", "Башня", "Инферно", "Некрополис", "Темница",
    "Цитадель", "Крепость", "Сопряжение", "Причал", "Фабрика",
    "Нейтралы", "Боевые машины"
)

@Composable
fun AppRoot(
    heroDao: HeroDao,
    creatureDao: CreatureDao,
    isMuted: Boolean,
    onMuteToggle: () -> Unit
) {
    var currentScreen by remember { mutableStateOf(Screen.MainMenu) }

    // Состояния для Героев
    var selectedHeroTown by remember { mutableStateOf<String?>(null) }
    var selectedHeroClassType by remember { mutableStateOf<String?>(null) }
    var selectedHero by remember { mutableStateOf<Hero?>(null) }

    // Состояния для Существ
    var selectedCreatureTown by remember { mutableStateOf<String?>(null) }
    var selectedCreature by remember { mutableStateOf<Creature?>(null) }

    // Состояния для Навыков
    var selectedSkill by remember { mutableStateOf<SecondarySkill?>(null) }

    // Состояния для Магии
    var selectedSchool by remember { mutableStateOf<String?>(null) }
    var selectedSpell by remember { mutableStateOf<Spell?>(null) }


    // Загрузка данных (Flow)
    val allHeroes by heroDao.getAllHeroes().collectAsState(initial = emptyList())
    val allCreatures by creatureDao.getAllCreatures().collectAsState(initial = emptyList())

    // Списки городов (сортированные)
    val heroTowns = remember(allHeroes) {
        allHeroes.map { it.town }.distinct().sortedBy { TOWN_ORDER.indexOf(it) }
    }
    val creatureTowns = remember(allCreatures) {
        allCreatures.map { it.town }.distinct().sortedBy { TOWN_ORDER.indexOf(it) }
    }

    when (currentScreen) {
        // --- ГЛАВНОЕ МЕНЮ ---
        Screen.MainMenu -> {
            MainMenuScreen(
                onHeroesClick = { currentScreen = Screen.HeroTowns },
                onCreaturesClick = { currentScreen = Screen.CreatureTowns },
                onSkillsClick = { currentScreen = Screen.SkillsList },
                onMagicClick = { currentScreen = Screen.MagicSchools }, // <-- Добавлено
                isMuted = isMuted,
                onMuteToggle = onMuteToggle
            )
        }

        // --- ВЕТКА ГЕРОЕВ ---
        Screen.HeroTowns -> {
            BackHandler { currentScreen = Screen.MainMenu }
            TownSelectionScreen(
                title = "Герои: Выбор города",
                towns = heroTowns,
                onBack = { currentScreen = Screen.MainMenu },
                onTownSelected = { town ->
                    selectedHeroTown = town
                    currentScreen = Screen.HeroClasses
                }
            )
        }
        Screen.HeroClasses -> {
            BackHandler { currentScreen = Screen.HeroTowns }
            if (selectedHeroTown != null) {
                val mightClass = allHeroes.firstOrNull { it.town == selectedHeroTown && it.classType == "Might" }?.heroClass ?: "Воин"
                val magicClass = allHeroes.firstOrNull { it.town == selectedHeroTown && it.classType == "Magic" }?.heroClass ?: "Маг"

                ClassSelectionScreen(
                    townName = selectedHeroTown!!,
                    mightClassName = mightClass,
                    magicClassName = magicClass,
                    onBack = { currentScreen = Screen.HeroTowns },
                    onClassSelected = { type ->
                        selectedHeroClassType = type
                        currentScreen = Screen.HeroList
                    }
                )
            }
        }
        Screen.HeroList -> {
            BackHandler { currentScreen = Screen.HeroClasses }
            if (selectedHeroTown != null && selectedHeroClassType != null) {
                val filtered = allHeroes.filter { it.town == selectedHeroTown && it.classType == selectedHeroClassType }
                HeroListScreen(
                    heroes = filtered,
                    townName = selectedHeroTown!!,
                    className = filtered.firstOrNull()?.heroClass ?: "",
                    onBack = { currentScreen = Screen.HeroClasses },
                    onHeroSelected = { h ->
                        selectedHero = h
                        currentScreen = Screen.HeroDetail
                    }
                )
            }
        }
        Screen.HeroDetail -> {
            BackHandler { currentScreen = Screen.HeroList }
            if (selectedHero != null) {
                HeroDetailScreen(
                    hero = selectedHero!!,
                    creatures = allCreatures,
                    onBack = { currentScreen = Screen.HeroList }
                )
            }
        }

        // --- ВЕТКА СУЩЕСТВ ---
        Screen.CreatureTowns -> {
            BackHandler { currentScreen = Screen.MainMenu }
            TownSelectionScreen(
                title = "Существа: Выбор фракции",
                towns = creatureTowns,
                onBack = { currentScreen = Screen.MainMenu },
                onTownSelected = { town ->
                    selectedCreatureTown = town
                    currentScreen = Screen.CreatureList
                }
            )
        }
        Screen.CreatureList -> {
            BackHandler { currentScreen = Screen.CreatureTowns }
            if (selectedCreatureTown != null) {
                val creatures = allCreatures.filter { it.town == selectedCreatureTown }
                CreatureListScreen(
                    townName = selectedCreatureTown!!,
                    creatures = creatures,
                    onBack = { currentScreen = Screen.CreatureTowns },
                    onCreatureSelected = { c ->
                        selectedCreature = c
                        currentScreen = Screen.CreatureDetail
                    }
                )
            }
        }
        Screen.CreatureDetail -> {
            BackHandler { currentScreen = Screen.CreatureList }
            if (selectedCreature != null) {
                CreatureDetailScreen(
                    creature = selectedCreature!!,
                    onBack = { currentScreen = Screen.CreatureList }
                )
            }
        }

        // --- ВЕТКА ВТОРИЧНЫХ НАВЫКОВ ---
        Screen.SkillsList -> {
            BackHandler { currentScreen = Screen.MainMenu }
            SecondarySkillsListScreen(
                skills = com.example.homm3reference.data.GameData.secondarySkills,
                onBack = { currentScreen = Screen.MainMenu },
                onSkillSelected = { skill ->
                    selectedSkill = skill
                    currentScreen = Screen.SkillDetail
                }
            )
        }
        Screen.SkillDetail -> {
            BackHandler { currentScreen = Screen.SkillsList }
            selectedSkill?.let { skill ->
                SecondarySkillDetailScreen(
                    skill = skill,
                    onBack = { currentScreen = Screen.SkillsList }
                )
            }
        }

        // --- ВЕТКА МАГИИ (НОВАЯ) ---
        Screen.MagicSchools -> {
            BackHandler { currentScreen = Screen.MainMenu }
            MagicSchoolSelectScreen(
                onBack = { currentScreen = Screen.MainMenu },
                onSchoolSelected = { school ->
                    selectedSchool = school
                    currentScreen = Screen.MagicList
                }
            )
        }
        Screen.MagicList -> {
            BackHandler { currentScreen = Screen.MagicSchools }
            if (selectedSchool != null) {
                val spells = com.example.homm3reference.data.GameData.spells.filter { it.school == selectedSchool }
                SpellListScreen(
                    school = selectedSchool!!,
                    spells = spells,
                    onBack = { currentScreen = Screen.MagicSchools },
                    onSpellSelected = { spell ->
                        selectedSpell = spell
                        currentScreen = Screen.MagicDetail
                    }
                )
            }
        }
        Screen.MagicDetail -> {
            BackHandler { currentScreen = Screen.MagicList }
            if (selectedSpell != null) {
                SpellDetailScreen(
                    spell = selectedSpell!!,
                    onBack = { currentScreen = Screen.MagicList }
                )
            }
        }
    }
}