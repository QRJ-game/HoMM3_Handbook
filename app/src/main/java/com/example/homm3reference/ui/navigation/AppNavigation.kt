package com.example.homm3reference.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import com.example.homm3reference.data.Creature
import com.example.homm3reference.data.CreatureDao
import com.example.homm3reference.data.Hero
import com.example.homm3reference.data.HeroDao
import com.example.homm3reference.ui.common.TownSelectionScreen
import com.example.homm3reference.ui.creatures.CreatureDetailScreen
import com.example.homm3reference.ui.creatures.CreatureListScreen
import com.example.homm3reference.ui.heroes.ClassSelectionScreen
import com.example.homm3reference.ui.heroes.HeroDetailScreen
import com.example.homm3reference.ui.heroes.HeroListScreen
import com.example.homm3reference.ui.main_menu.MainMenuScreen

enum class Screen {
    MainMenu,
    HeroTowns, HeroClasses, HeroList, HeroDetail,
    CreatureTowns, CreatureList, CreatureDetail
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

    var selectedHeroTown by remember { mutableStateOf<String?>(null) }
    var selectedHeroClassType by remember { mutableStateOf<String?>(null) }
    var selectedHero by remember { mutableStateOf<Hero?>(null) }

    var selectedCreatureTown by remember { mutableStateOf<String?>(null) }
    var selectedCreature by remember { mutableStateOf<Creature?>(null) }

    val allHeroes by heroDao.getAllHeroes().collectAsState(initial = emptyList())
    val allCreatures by creatureDao.getAllCreatures().collectAsState(initial = emptyList())

    val heroTowns = remember(allHeroes) {
        allHeroes.map { it.town }.distinct().sortedBy { TOWN_ORDER.indexOf(it) }
    }
    val creatureTowns = remember(allCreatures) {
        allCreatures.map { it.town }.distinct().sortedBy { TOWN_ORDER.indexOf(it) }
    }

    when (currentScreen) {
        Screen.MainMenu -> {
            MainMenuScreen(
                onHeroesClick = { currentScreen = Screen.HeroTowns },
                onCreaturesClick = { currentScreen = Screen.CreatureTowns },
                isMuted = isMuted,
                onMuteToggle = onMuteToggle
            )
        }

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
                    onBack = { currentScreen = Screen.HeroList }
                )
            }
        }

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
    }
}