package com.example.homm3reference.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TownSelectionScreen(
    title: String,
    towns: List<String>,
    onBack: () -> Unit,
    onTownSelected: (String) -> Unit
) {
    AppBackground {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(towns) { town ->
                    TownCard(townName = town, onClick = { onTownSelected(town) })
                }
            }
        }
    }
}