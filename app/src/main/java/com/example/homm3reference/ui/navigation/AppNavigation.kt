package com.example.homm3reference.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.homm3reference.data.*
import com.example.homm3reference.ui.artifacts.*
import com.example.homm3reference.ui.common.TownSelectionScreen
import com.example.homm3reference.ui.creatures.*
import com.example.homm3reference.ui.heroes.*
import com.example.homm3reference.ui.magic.*
import com.example.homm3reference.ui.main_menu.MainMenuScreen
import com.example.homm3reference.ui.skills.*
import com.example.homm3reference.ui.utils.*

enum class Screen {
    MainMenu,
    HeroTowns, HeroClasses, HeroList, HeroDetail,
    CreatureTowns, CreatureList, CreatureDetail,
    SkillsList, SkillDetail,
    MagicSchools, MagicList, MagicDetail,
    ArtifactsMenu, ArtifactsCategory, ArtifactsList, ArtifactDetail,

    UtilitiesMenu, UtilityUpgradeCheck
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

    // Герои
    var selectedHeroTown by remember { mutableStateOf<String?>(null) }
    var selectedHeroClassType by remember { mutableStateOf<String?>(null) }
    var selectedHero by remember { mutableStateOf<Hero?>(null) }
    var heroesSearchQuery by remember { mutableStateOf("") }

    // Существа
    var selectedCreatureTown by remember { mutableStateOf<String?>(null) }
    var selectedCreature by remember { mutableStateOf<Creature?>(null) }
    var creaturesSearchQuery by remember { mutableStateOf("") }

    // Навыки (Добавлено skillsSearchQuery)
    var selectedSkill by remember { mutableStateOf<SecondarySkill?>(null) }
    var skillsSearchQuery by remember { mutableStateOf("") }

    // Магия
    var selectedSchool by remember { mutableStateOf<String?>(null) }
    var selectedSpell by remember { mutableStateOf<Spell?>(null) }
    var magicSearchQuery by remember { mutableStateOf("") }

    // Артефакты
    var selectedArtifactCategoryType by remember { mutableStateOf<String?>(null) }
    var selectedArtifactCategoryValue by remember { mutableStateOf<String?>(null) }
    var selectedArtifact by remember { mutableStateOf<Artifact?>(null) }
    var artifactsSearchQuery by remember { mutableStateOf("") }

    // Данные
    val allHeroes by heroDao.getAllHeroes().collectAsState(initial = emptyList())
    val allCreatures by creatureDao.getAllCreatures().collectAsState(initial = emptyList())
    val allSpells = GameData.spells

    val heroTowns = remember(allHeroes) { allHeroes.map { it.town }.distinct().sortedBy { TOWN_ORDER.indexOf(it) } }
    val creatureTowns = remember(allCreatures) { allCreatures.map { it.town }.distinct().sortedBy { TOWN_ORDER.indexOf(it) } }

    LaunchedEffect(allCreatures, allHeroes) {
        if (allCreatures.isNotEmpty()) GameData.creatures = allCreatures
        if (allHeroes.isNotEmpty() && GameData.creatures.isNotEmpty() && GameData.spells.isNotEmpty()) {
            GameData.checkMissingSpecialtyIcons(allHeroes)
        }
    }

    when (currentScreen) {
        Screen.MainMenu -> {
            MainMenuScreen(
                onHeroesClick = { heroesSearchQuery = ""; selectedHeroTown = null; currentScreen = Screen.HeroTowns },
                onCreaturesClick = { creaturesSearchQuery = ""; selectedCreatureTown = null; currentScreen = Screen.CreatureTowns },
                onSkillsClick = { skillsSearchQuery = ""; currentScreen = Screen.SkillsList },
                onMagicClick = { magicSearchQuery = ""; selectedSchool = null; currentScreen = Screen.MagicSchools },
                onArtifactsClick = { artifactsSearchQuery = ""; currentScreen = Screen.ArtifactsMenu },
                onUtilitiesClick = { currentScreen = Screen.UtilitiesMenu },
                isMuted = isMuted,
                onMuteToggle = onMuteToggle
            )
        }

        // --- HEROES ---
        Screen.HeroTowns -> {
            BackHandler { currentScreen = Screen.MainMenu }
            val filtered = if (heroesSearchQuery.isBlank()) emptyList() else allHeroes.filter { it.name.contains(heroesSearchQuery, true) }
            TownSelectionScreen(
                title = "Герои: Выбор города",
                towns = heroTowns,
                onTownSelected = { selectedHeroTown = it; currentScreen = Screen.HeroClasses },
                searchQuery = heroesSearchQuery,
                onQueryChanged = { heroesSearchQuery = it },
                searchResultsContent = {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 16.dp)) {
                        items(filtered) { hero -> HeroCard(hero) { selectedHero = it; selectedHeroTown = null; currentScreen = Screen.HeroDetail } }
                    }
                }
            )
        }
        Screen.HeroClasses -> {
            BackHandler { selectedHeroTown = null; currentScreen = Screen.HeroTowns }
            selectedHeroTown?.let { town ->
                val m1 = allHeroes.firstOrNull { it.town == town && it.classType == "Might" }?.heroClass ?: "Воин"
                val m2 = allHeroes.firstOrNull { it.town == town && it.classType == "Magic" }?.heroClass ?: "Маг"
                ClassSelectionScreen(town, m1, m2) { selectedHeroClassType = it; currentScreen = Screen.HeroList }
            }
        }
        Screen.HeroList -> {
            BackHandler { currentScreen = Screen.HeroClasses }
            if (selectedHeroTown != null && selectedHeroClassType != null) {
                val list = allHeroes.filter { it.town == selectedHeroTown && it.classType == selectedHeroClassType }
                HeroListScreen(list, selectedHeroTown!!, list.firstOrNull()?.heroClass ?: "") { selectedHero = it; currentScreen = Screen.HeroDetail }
            }
        }
        Screen.HeroDetail -> {
            BackHandler { if (selectedHeroTown == null) currentScreen = Screen.HeroTowns else currentScreen = Screen.HeroList }
            selectedHero?.let { HeroDetailScreen(it, allCreatures) }
        }

        // --- CREATURES ---
        Screen.CreatureTowns -> {
            BackHandler { currentScreen = Screen.MainMenu }
            val filtered = if (creaturesSearchQuery.isBlank()) emptyList() else allCreatures.filter { it.name.contains(creaturesSearchQuery, true) }
            TownSelectionScreen(
                title = "Существа: Выбор фракции",
                towns = creatureTowns,
                onTownSelected = { selectedCreatureTown = it; currentScreen = Screen.CreatureList },
                searchQuery = creaturesSearchQuery,
                onQueryChanged = { creaturesSearchQuery = it },
                searchResultsContent = {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 16.dp)) {
                        items(filtered) { c -> CreatureCard(c) { selectedCreature = it; selectedCreatureTown = null; currentScreen = Screen.CreatureDetail } }
                    }
                }
            )
        }
        Screen.CreatureList -> {
            BackHandler { selectedCreatureTown = null; currentScreen = Screen.CreatureTowns }
            selectedCreatureTown?.let { town ->
                CreatureListScreen(town, allCreatures.filter { it.town == town }) { selectedCreature = it; currentScreen = Screen.CreatureDetail }
            }
        }
        Screen.CreatureDetail -> {
            BackHandler { if (selectedCreatureTown == null) currentScreen = Screen.CreatureTowns else currentScreen = Screen.CreatureList }
            selectedCreature?.let { CreatureDetailScreen(it) }
        }

        // --- SKILLS ---
        Screen.SkillsList -> {
            BackHandler { currentScreen = Screen.MainMenu }
            val filtered = if (skillsSearchQuery.isBlank()) GameData.secondarySkills else GameData.secondarySkills.filter { it.name.contains(skillsSearchQuery, true) }

            // ВОТ ЗДЕСЬ БЫЛА ОШИБКА, ТЕПЕРЬ ПАРАМЕТРЫ СОВПАДАЮТ
            SecondarySkillsListScreen(
                skills = filtered,
                onSkillClick = { selectedSkill = it; currentScreen = Screen.SkillDetail },
                searchQuery = skillsSearchQuery,
                onQueryChanged = { skillsSearchQuery = it }
            )
        }
        Screen.SkillDetail -> {
            BackHandler { currentScreen = Screen.SkillsList }
            selectedSkill?.let { SecondarySkillDetailScreen(it) }
        }

        // --- MAGIC ---
        Screen.MagicSchools -> {
            BackHandler { currentScreen = Screen.MainMenu }
            val filtered = if (magicSearchQuery.isBlank()) emptyList() else allSpells.filter { it.name.contains(magicSearchQuery, true) }
            MagicSchoolSelectScreen(
                onSchoolSelected = { selectedSchool = it; currentScreen = Screen.MagicList },
                searchQuery = magicSearchQuery,
                onQueryChanged = { magicSearchQuery = it },
                searchResultsContent = {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 16.dp)) {
                        items(filtered) { s -> SpellCard(s) { selectedSpell = s; selectedSchool = null; currentScreen = Screen.MagicDetail } }
                    }
                }
            )
        }
        Screen.MagicList -> {
            BackHandler { selectedSchool = null; currentScreen = Screen.MagicSchools }
            selectedSchool?.let { school ->
                SpellListScreen(school, allSpells.filter { it.school.contains(school, true) }) { selectedSpell = it; currentScreen = Screen.MagicDetail }
            }
        }
        Screen.MagicDetail -> {
            BackHandler { if (selectedSchool == null) currentScreen = Screen.MagicSchools else currentScreen = Screen.MagicList }
            selectedSpell?.let { SpellDetailScreen(it) }
        }

        // --- ARTIFACTS ---
        Screen.ArtifactsMenu -> {
            BackHandler { currentScreen = Screen.MainMenu }
            val filtered = if (artifactsSearchQuery.isBlank()) emptyList() else GameData.artifacts.filter { it.name.contains(artifactsSearchQuery, true) }
            ArtifactsMenuScreen(
                onCategoryClick = { selectedArtifactCategoryType = it; currentScreen = Screen.ArtifactsCategory },
                searchQuery = artifactsSearchQuery,
                onQueryChanged = { artifactsSearchQuery = it },
                searchResultsContent = {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 16.dp)) {
                        items(filtered) { a -> ArtifactCard(a) { selectedArtifact = a; currentScreen = Screen.ArtifactDetail } }
                    }
                }
            )
        }
        Screen.ArtifactsCategory -> {
            BackHandler { currentScreen = Screen.ArtifactsMenu }
            selectedArtifactCategoryType?.let { type ->
                ArtifactCategorySelectScreen(type) { selectedArtifactCategoryValue = it; currentScreen = Screen.ArtifactsList }
            }
        }
        Screen.ArtifactsList -> {
            BackHandler { currentScreen = Screen.ArtifactsCategory }
            val list = GameData.artifacts.filter {
                when (selectedArtifactCategoryType) {
                    "class" -> it.classType.equals(selectedArtifactCategoryValue, true)
                    "slot" -> it.slot.equals(selectedArtifactCategoryValue, true)
                    "group" -> it.group.equals(selectedArtifactCategoryValue, true)
                    else -> true
                }
            }
            ArtifactListScreen(list) { selectedArtifact = it; currentScreen = Screen.ArtifactDetail }
        }
        Screen.ArtifactDetail -> {
            BackHandler { currentScreen = if (artifactsSearchQuery.isNotBlank()) Screen.ArtifactsMenu else Screen.ArtifactsList }
            selectedArtifact?.let { ArtifactDetailScreen(it) { newA -> selectedArtifact = newA } }
        }
        Screen.UtilitiesMenu -> {
            BackHandler { currentScreen = Screen.MainMenu }
            // Не забудьте импортировать UtilitiesMenuScreen
            com.example.homm3reference.ui.utils.UtilitiesMenuScreen(
                onUpgradeCheckerClick = { currentScreen = Screen.UtilityUpgradeCheck }
            )
        }
        Screen.UtilityUpgradeCheck -> {
            BackHandler { currentScreen = Screen.UtilitiesMenu }
            // Не забудьте импортировать CreatureUpgradeCheckerScreen
            com.example.homm3reference.ui.utils.CreatureUpgradeCheckerScreen()
        }
    }
}