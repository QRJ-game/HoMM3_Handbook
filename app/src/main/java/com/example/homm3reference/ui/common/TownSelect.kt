package com.example.homm3reference.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TownSelectionScreen(
    title: String,
    towns: List<String>,
    onTownSelected: (String) -> Unit,
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    searchResultsContent: @Composable () -> Unit
) {
    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {

            // Заголовок и поиск
            Column(modifier = Modifier.padding(16.dp)) {
               // Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD4AF37),
                    modifier = Modifier.padding( top = 32.dp, bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
                )

                //Spacer(modifier = Modifier.height(16.dp))

                AppSearchBar(
                    query = searchQuery,
                    onQueryChanged = onQueryChanged,
                    placeholderText = "Поиск..."
                )
            }

            if (searchQuery.isNotBlank()) {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    searchResultsContent()
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(towns) { town ->
                        TownCard(townName = town, onClick = { onTownSelected(town) })
                    }
                }
            }
        }
    }
}