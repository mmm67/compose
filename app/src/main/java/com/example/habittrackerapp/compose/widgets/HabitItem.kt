package com.example.habittrackerapp.compose.widgets

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.habittrackerapp.utilities.Constants
import com.example.habittrackerapp.HabitEvent
import com.example.habittrackerapp.data.Category
import com.example.habittrackerapp.data.Habit

@Composable
fun HabitItem(
    habitCategory: Category,
    habit: Habit,
    onEvent: (HabitEvent) -> Unit,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = calculateProgress(habit.frequency, habit.hitCount).toFloat(),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xffc9def4),
                            Color(0xfff5ccd4),
                            Color(0xffb8a4c9)
                        )
                    )
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically // Align items vertically center
        ) {
            // Circular Progress Indicator with text in the center
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(70.dp)
                    .padding(10.dp)
            ) {
                CircularProgressIndicator(
                    progress = { animatedProgress/100f },
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color(0xFCBEA2B0),
                )
                AutoResizedText(
                    text = "${habit.hitCount}/${getMaxHits(habit.frequency)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
            }

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.headlineMedium
                )
                if (habit.description.isNotEmpty()) {
                    Text(
                        color = Color.Gray,
                        text = habit.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Text(
                    color = Color.Black,
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Category: ")
                        }
                        append(habitCategory.name)
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            onEvent(HabitEvent.DeleteHabit(habit))
                        },
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFC12E6B)
                )
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            onEvent(HabitEvent.IncrementHitCount(habit.id, habit.frequency))
                        },
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Done",
                    tint = Color.Black
                )
            }
        }
    }
}

fun calculateProgress(frequency: String, hitCount: Int): Float {
    val maxHits = getMaxHits(frequency)
    return if (maxHits == 0) 0f else (hitCount.toFloat() / maxHits.toFloat()) * 100f
}

fun getMaxHits(frequency: String): Int {
    return when (frequency) {
        Constants.HabitFrequency.DAILY -> 365
        Constants.HabitFrequency.WEEKLY -> 48
        Constants.HabitFrequency.MONTHLY -> 12
        else -> 0
    }
}
