package com.example.habittrackerapp.compose.widgets

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.habittrackerapp.HabitEvent
import com.example.habittrackerapp.data.Category
import com.example.habittrackerapp.data.Habit

@Composable
fun HabitsListComponents(
    items: List<Habit>,
    categoryMap: Map<Int, Category>,
    onItemClicked: (HabitEvent) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items, key = { it.id }) { habit ->
            val category = categoryMap[habit.categoryId]
            if (category != null) {
                HabitItem(
                    habitCategory = category,
                    habit = habit,
                    onEvent = {
                        onItemClicked(it)
                    })
            }
        }
    }
}
