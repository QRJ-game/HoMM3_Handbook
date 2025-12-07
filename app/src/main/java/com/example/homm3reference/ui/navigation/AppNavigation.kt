package com.example.homm3reference.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.homm3reference.data.Creature
import com.example.homm3reference.data.CreatureDao
import com.example.homm3reference.data.GameData
import com.example.homm3reference.data.Hero
import com.example.homm3reference.data.HeroDao
import com.example.homm3reference.data.SecondarySkill
import com.example.homm3reference.data.Spell
import com.example.homm3reference.ui.common.TownSelectionScreen
import com.example.homm3reference.ui.creatures.CreatureCard
import com.example.homm3reference.ui.creatures.CreatureDetailScreen
import com.example.homm3reference.ui.creatures.CreatureListScreen
import com.example.homm3reference.ui.heroes.ClassSelectionScreen
import com.example.homm3reference.ui.heroes.HeroCard
import com.example.homm3reference.ui.heroes.HeroDetailScreen
import com.example.homm3reference.ui.heroes.HeroListScreen
import com.example.homm3reference.ui.magic.MagicSchoolSelectScreen
import com.example.homm3reference.ui.magic.SpellCard
import com.example.homm3reference.ui.magic.SpellDetailScreen
import com.example.homm3reference.ui.magic.SpellListScreen
import com.example.homm3reference.ui.main_menu.MainMenuScreen
import com.example.homm3reference.ui.skills.SecondarySkillDetailScreen
import com.example.homm3reference.ui.skills.SecondarySkillsListScreen

enum class Screen {
    MainMenu,
    HeroTowns, HeroClasses, HeroList, HeroDetail,
    CreatureTowns, CreatureList, CreatureDetail,
    SkillsList, SkillDetail,
    MagicSchools, MagicList, MagicDetail
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
    // Поиск Героев
    var heroesSearchQuery by remember { mutableStateOf("") }

    // Состояния для Существ
    var selectedCreatureTown by remember { mutableStateOf<String?>(null) }
    var selectedCreature by remember { mutableStateOf<Creature?>(null) }
    // Поиск Существ
    var creaturesSearchQuery by remember { mutableStateOf("") }

    // Состояния для Навыков
    var selectedSkill by remember { mutableStateOf<SecondarySkill?>(null) }

    // Состояния для Магии
    var selectedSchool by remember { mutableStateOf<String?>(null) }
    var selectedSpell by remember { mutableStateOf<Spell?>(null) }
    // Поиск Заклинаний
    var magicSearchQuery by remember { mutableStateOf("") }


    // Загрузка данных (Flow)
    val allHeroes by heroDao.getAllHeroes().collectAsState(initial = emptyList())
    val allCreatures by creatureDao.getAllCreatures().collectAsState(initial = emptyList())

    // FIX: Не используем remember для GameData.spells, чтобы данные обновлялись при поиске
    val allSpells = GameData.spells

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
                onHeroesClick = {
                    heroesSearchQuery = ""
                    currentScreen = Screen.HeroTowns
                },
                onCreaturesClick = {
                    creaturesSearchQuery = ""
                    currentScreen = Screen.CreatureTowns
                },
                onSkillsClick = { currentScreen = Screen.SkillsList },
                onMagicClick = {
                    magicSearchQuery = ""
                    currentScreen = Screen.MagicSchools
                },
                isMuted = isMuted,
                onMuteToggle = onMuteToggle
            )
        }

        // --- ВЕТКА ГЕРОЕВ ---
        Screen.HeroTowns -> {
            BackHandler { currentScreen = Screen.MainMenu }

            val filteredHeroes = remember(allHeroes, heroesSearchQuery) {
                if (heroesSearchQuery.isBlank()) emptyList()
                else allHeroes.filter { it.name.contains(heroesSearchQuery, ignoreCase = true) }
            }

            TownSelectionScreen(
                title = "Герои: Выбор города",
                towns = heroTowns,
                onTownSelected = { town ->
                    selectedHeroTown = town
                    currentScreen = Screen.HeroClasses
                },
                searchQuery = heroesSearchQuery,
                onQueryChanged = { heroesSearchQuery = it },
                searchResultsContent = {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(filteredHeroes) { hero ->
                            HeroCard(hero = hero, onHeroSelected = {
                                selectedHero = it
                                currentScreen = Screen.HeroDetail
                            })
                        }
                    }
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
            BackHandler {
                if (selectedHeroTown == null) currentScreen = Screen.HeroTowns else currentScreen = Screen.HeroList
            }
            if (selectedHero != null) {
                HeroDetailScreen(
                    hero = selectedHero!!,
                    creatures = allCreatures,
                    onBack = {
                        if (selectedHeroTown == null) currentScreen = Screen.HeroTowns else currentScreen = Screen.HeroList
                    }
                )
            }
        }

        // --- ВЕТКА СУЩЕСТВ ---
        Screen.CreatureTowns -> {
            BackHandler { currentScreen = Screen.MainMenu }

            val filteredCreatures = remember(allCreatures, creaturesSearchQuery) {
                if (creaturesSearchQuery.isBlank()) emptyList()
                else allCreatures.filter { it.name.contains(creaturesSearchQuery, ignoreCase = true) }
            }

            TownSelectionScreen(
                title = "Существа: Выбор фракции",
                towns = creatureTowns,
                onTownSelected = { town ->
                    selectedCreatureTown = town
                    currentScreen = Screen.CreatureList
                },
                searchQuery = creaturesSearchQuery,
                onQueryChanged = { creaturesSearchQuery = it },
                searchResultsContent = {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(filteredCreatures) { creature ->
                            CreatureCard(creature = creature, onClick = {
                                selectedCreature = it
                                currentScreen = Screen.CreatureDetail
                            })
                        }
                    }
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
            BackHandler {
                if (selectedCreatureTown == null) currentScreen = Screen.CreatureTowns else currentScreen = Screen.CreatureList
            }
            if (selectedCreature != null) {
                CreatureDetailScreen(
                    creature = selectedCreature!!,
                    onBack = {
                        if (selectedCreatureTown == null) currentScreen = Screen.CreatureTowns else currentScreen = Screen.CreatureList
                    }
                )
            }
        }

        // --- ВЕТКА ВТОРИЧНЫХ НАВЫКОВ ---
        Screen.SkillsList -> {
            BackHandler { currentScreen = Screen.MainMenu }
            SecondarySkillsListScreen(
                skills = GameData.secondarySkills,
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

        // --- ВЕТКА МАГИИ ---
        Screen.MagicSchools -> {
            BackHandler { currentScreen = Screen.MainMenu }

            // Фильтрация заклинаний (используем allSpells без remember для актуальности данных)
            val filteredSpells = remember(allSpells, magicSearchQuery) {
                if (magicSearchQuery.isBlank()) emptyList()
                else allSpells.filter { it.name.contains(magicSearchQuery, ignoreCase = true) }
            }

            MagicSchoolSelectScreen(
                onSchoolSelected = { school ->
                    selectedSchool = school
                    currentScreen = Screen.MagicList
                },
                searchQuery = magicSearchQuery,
                onQueryChanged = { magicSearchQuery = it },
                searchResultsContent = {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(filteredSpells) { spell ->
                            SpellCard(spell = spell, onClick = {
                                selectedSpell = spell
                                currentScreen = Screen.MagicDetail
                            })
                        }
                    }
                }
            )
        }
        Screen.MagicList -> {
            BackHandler { currentScreen = Screen.MagicSchools }
            if (selectedSchool != null) {
                val spells = allSpells.filter { it.school == selectedSchool }
                SpellListScreen(
                    school = selectedSchool!!,
                    spells = spells,
                    onSpellSelected = { spell ->
                        selectedSpell = spell
                        currentScreen = Screen.MagicDetail
                    }
                )
            }
        }
        Screen.MagicDetail -> {
            BackHandler {
                if (selectedSchool == null) currentScreen = Screen.MagicSchools else currentScreen = Screen.MagicList
            }
            if (selectedSpell != null) {
                SpellDetailScreen(
                    spell = selectedSpell!!
                )
            }
        }
    }
}