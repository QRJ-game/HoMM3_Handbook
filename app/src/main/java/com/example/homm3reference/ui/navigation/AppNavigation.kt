package com.example.homm3reference.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.homm3reference.data.Artifact
import com.example.homm3reference.data.Creature
import com.example.homm3reference.data.CreatureDao
import com.example.homm3reference.data.GameData
import com.example.homm3reference.data.Hero
import com.example.homm3reference.data.HeroDao
import com.example.homm3reference.data.SecondarySkill
import com.example.homm3reference.data.Spell
import com.example.homm3reference.ui.artifacts.ArtifactCard
import com.example.homm3reference.ui.artifacts.ArtifactCategorySelectScreen
import com.example.homm3reference.ui.artifacts.ArtifactDetailScreen
import com.example.homm3reference.ui.artifacts.ArtifactListScreen
import com.example.homm3reference.ui.artifacts.ArtifactsMenuScreen
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
    MagicSchools, MagicList, MagicDetail,
    // Артефакты
    ArtifactsMenu, ArtifactsCategory, ArtifactsList, ArtifactDetail
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
    var heroesSearchQuery by remember { mutableStateOf("") }

    // Состояния для Существ
    var selectedCreatureTown by remember { mutableStateOf<String?>(null) }
    var selectedCreature by remember { mutableStateOf<Creature?>(null) }
    var creaturesSearchQuery by remember { mutableStateOf("") }

    // Состояния для Навыков
    var selectedSkill by remember { mutableStateOf<SecondarySkill?>(null) }

    // Состояния для Магии
    var selectedSchool by remember { mutableStateOf<String?>(null) }
    var selectedSpell by remember { mutableStateOf<Spell?>(null) }
    var magicSearchQuery by remember { mutableStateOf("") }

    // Состояния для Артефактов
    var selectedArtifactCategoryType by remember { mutableStateOf<String?>(null) }
    var selectedArtifactCategoryValue by remember { mutableStateOf<String?>(null) }
    var selectedArtifact by remember { mutableStateOf<Artifact?>(null) }
    // --- Добавлен поиск для артефактов ---
    var artifactsSearchQuery by remember { mutableStateOf("") }

    // Загрузка данных
    val allHeroes by heroDao.getAllHeroes().collectAsState(initial = emptyList())
    val allCreatures by creatureDao.getAllCreatures().collectAsState(initial = emptyList())
    val allSpells = GameData.spells

    val heroTowns = remember(allHeroes) {
        allHeroes.map { it.town }.distinct().sortedBy { TOWN_ORDER.indexOf(it) }
    }
    val creatureTowns = remember(allCreatures) {
        allCreatures.map { it.town }.distinct().sortedBy { TOWN_ORDER.indexOf(it) }
    }

    LaunchedEffect(allCreatures, allHeroes) {
        if (allCreatures.isNotEmpty()) {
            GameData.creatures = allCreatures
        }
        if (allHeroes.isNotEmpty() &&
            GameData.creatures.isNotEmpty() &&
            GameData.spells.isNotEmpty() &&
            GameData.secondarySkills.isNotEmpty()) {
            GameData.checkMissingSpecialtyIcons(allHeroes)
        }
    }

    when (currentScreen) {
        Screen.MainMenu -> {
            MainMenuScreen(
                onHeroesClick = {
                    heroesSearchQuery = ""
                    selectedHeroTown = null
                    currentScreen = Screen.HeroTowns
                },
                onCreaturesClick = {
                    creaturesSearchQuery = ""
                    selectedCreatureTown = null
                    currentScreen = Screen.CreatureTowns
                },
                onSkillsClick = { currentScreen = Screen.SkillsList },
                onMagicClick = {
                    magicSearchQuery = ""
                    selectedSchool = null
                    currentScreen = Screen.MagicSchools
                },
                onArtifactsClick = {
                    artifactsSearchQuery = "" // Сброс поиска при входе
                    currentScreen = Screen.ArtifactsMenu
                },
                isMuted = isMuted,
                onMuteToggle = onMuteToggle
            )
        }

        // --- ГЕРОИ ---
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
                                selectedHeroTown = null
                                currentScreen = Screen.HeroDetail
                            })
                        }
                    }
                }
            )
        }
        Screen.HeroClasses -> {
            BackHandler {
                selectedHeroTown = null
                currentScreen = Screen.HeroTowns
            }
            if (selectedHeroTown != null) {
                val mightClass = allHeroes.firstOrNull { it.town == selectedHeroTown && it.classType == "Might" }?.heroClass ?: "Воин"
                val magicClass = allHeroes.firstOrNull { it.town == selectedHeroTown && it.classType == "Magic" }?.heroClass ?: "Маг"
                ClassSelectionScreen(
                    townName = selectedHeroTown!!,
                    mightClassName = mightClass,
                    magicClassName = magicClass,
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
            selectedHero?.let { HeroDetailScreen(hero = it, creatures = allCreatures) }
        }

        // --- СУЩЕСТВА ---
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
                                selectedCreatureTown = null
                                currentScreen = Screen.CreatureDetail
                            })
                        }
                    }
                }
            )
        }
        Screen.CreatureList -> {
            BackHandler {
                selectedCreatureTown = null
                currentScreen = Screen.CreatureTowns
            }
            if (selectedCreatureTown != null) {
                val creatures = allCreatures.filter { it.town == selectedCreatureTown }
                CreatureListScreen(
                    townName = selectedCreatureTown!!,
                    creatures = creatures,
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
            selectedCreature?.let { CreatureDetailScreen(creature = it) }
        }

        // --- НАВЫКИ ---
        Screen.SkillsList -> {
            BackHandler { currentScreen = Screen.MainMenu }
            SecondarySkillsListScreen(
                skills = GameData.secondarySkills,
                onSkillSelected = { skill ->
                    selectedSkill = skill
                    currentScreen = Screen.SkillDetail
                }
            )
        }
        Screen.SkillDetail -> {
            BackHandler { currentScreen = Screen.SkillsList }
            selectedSkill?.let { SecondarySkillDetailScreen(skill = it) }
        }

        // --- МАГИЯ ---
        Screen.MagicSchools -> {
            BackHandler { currentScreen = Screen.MainMenu }
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
                                selectedSchool = null
                                currentScreen = Screen.MagicDetail
                            })
                        }
                    }
                }
            )
        }
        Screen.MagicList -> {
            BackHandler {
                selectedSchool = null
                currentScreen = Screen.MagicSchools
            }
            if (selectedSchool != null) {
                val spells = allSpells.filter { it.school.contains(selectedSchool!!, ignoreCase = true) }
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
            selectedSpell?.let { SpellDetailScreen(spell = it) }
        }

        // --- АРТЕФАКТЫ ---
        Screen.ArtifactsMenu -> {
            BackHandler { currentScreen = Screen.MainMenu }

            // Фильтрация для поиска в главном меню
            val searchResults = remember(GameData.artifacts, artifactsSearchQuery) {
                if (artifactsSearchQuery.isBlank()) emptyList()
                else GameData.artifacts.filter { it.name.contains(artifactsSearchQuery, ignoreCase = true) }
            }

            ArtifactsMenuScreen(
                onCategoryClick = { type ->
                    selectedArtifactCategoryType = type
                    currentScreen = Screen.ArtifactsCategory
                },
                searchQuery = artifactsSearchQuery,
                onQueryChanged = { artifactsSearchQuery = it },
                searchResultsContent = {
                    // Здесь отображаем результаты поиска
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(searchResults) { artifact ->
                            ArtifactCard(artifact = artifact) {
                                selectedArtifact = artifact
                                // Скрываем поиск при переходе, чтобы вернуться в меню
                                // или оставляем как есть.
                                currentScreen = Screen.ArtifactDetail
                            }
                        }
                    }
                }
            )
        }
        Screen.ArtifactsCategory -> {
            BackHandler { currentScreen = Screen.ArtifactsMenu }
            if (selectedArtifactCategoryType != null) {
                ArtifactCategorySelectScreen(
                    categoryType = selectedArtifactCategoryType!!,
                    onValueClick = { value ->
                        selectedArtifactCategoryValue = value
                        currentScreen = Screen.ArtifactsList
                    }
                )
            }
        }
        Screen.ArtifactsList -> {
            BackHandler { currentScreen = Screen.ArtifactsCategory }

            val filteredArtifacts = remember(selectedArtifactCategoryType, selectedArtifactCategoryValue) {
                GameData.artifacts.filter { artifact ->
                    when (selectedArtifactCategoryType) {
                        "class" -> artifact.classType.equals(selectedArtifactCategoryValue, ignoreCase = true)
                        "slot" -> artifact.slot.equals(selectedArtifactCategoryValue, ignoreCase = true)
                        "group" -> artifact.group.equals(selectedArtifactCategoryValue, ignoreCase = true)
                        else -> true
                    }
                }
            }

            ArtifactListScreen(
                artifacts = filteredArtifacts,
                onArtifactClick = { artifact ->
                    selectedArtifact = artifact
                    currentScreen = Screen.ArtifactDetail
                }
            )
        }
        Screen.ArtifactDetail -> {
            BackHandler {
                if (artifactsSearchQuery.isNotBlank()) {
                    currentScreen = Screen.ArtifactsMenu
                } else {
                    // Если мы "провалились" глубоко (компонент -> сборный),
                    // кнопка назад вернет нас в список.
                    // Можно усложнить логику стека, но пока вернемся в список.
                    currentScreen = Screen.ArtifactsList
                }
            }
            selectedArtifact?.let { artifact ->
                ArtifactDetailScreen(
                    artifact = artifact,
                    onArtifactClick = { newArtifact ->
                        // Переход на новый артефакт (например, с компонента на сборный или наоборот)
                        selectedArtifact = newArtifact
                    }
                )
            }
        }
    }
}