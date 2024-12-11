package com.example.habittrackerapp.compose.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ColorPickerDialog(
    onColorSelected: (Long) -> Unit,
    onDismissRequest: () -> Unit
) {
    val colors = listOf(
        (0xFFE1BEE7), // Light Lavender
        (0xFF9C27B0), // Purple
        (0xFFFFEBEE), // Light Pink
        (0xFFD32F2F), // Red
        (0xFF1976D2), // Blue
        (0xFF4CAF50), // Green
        (0xFFFFEB3B), // Yellow
        (0xFF8E24AA), // Deep Purple
        (0xFF00BCD4), // Cyan
        (0xFF0288D1), // Light Blue
        (0xFFFF9800), // Orange
        (0xFF8BC34A), // Light Green
        (0xFF607D8B), // Blue Grey
        (0xFF7B1FA2), // Purple
        (0xFFCDDC39), // Lime
        (0xFFFF5722), // Deep Orange
        (0xFFB2FF59), // Lime Green
        (0xFF9E9E9E), // Grey
        (0xFF3F51B5), // Indigo
        (0xFF2196F3), // Blue
        (0xFFF44336), // Red
        (0xFF607D8B), // Blue Grey
        (0xFF00C853), // Green
        (0xFF0288D1), // Blue
        (0xFF512DA8), // Deep Purple
        (0xFF795548), // Brown
        (0xFF512DA8), // Deep Purple
        (0xFF8E24AA), // Purple
        (0xFF009688), // Teal
        (0xFF4CAF50), // Green
        (0xFFFFC107), // Amber
        (0xFF3F51B5), // Indigo
        (0xFFFF9800), // Orange
        (0xFF8BC34A), // Light Green
        (0xFFCDDC39), // Lime
        (0xFF9E9E9E), // Grey
        (0xFF000000), // Black
        (0xFFFFFFFF), // White
        (0xFFB3E5FC), // Light Sky Blue
        (0xFFF1F8E9), // Light Lime
        (0xFFF44336), // Red
        (0xFF8D6E63), // Brown
        (0xFF5D4037), // Dark Brown
        (0xFF00695C), // Teal Dark
        (0xFF9E9E9E), // Grey
        (0xFF0288D1), // Light Blue
        (0xFFB2EBF2), // Light Turquoise
        (0xFFE8F5E9)  // Light Mint
    )


    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.3f)
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color = Color.White)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(colors) { color ->
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color(color))
                            .clickable {
                                onColorSelected(color)
                                onDismissRequest()
                            }
                    )
                }
            }

        }
    }
}
