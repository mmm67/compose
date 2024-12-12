package com.example.habittrackerapp.compose.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.habittrackerapp.compose.NavTopBar
import com.example.habittrackerapp.viewmodels.AppViewModel
import com.example.habittrackerapp.compose.Routes
import com.example.habittrackerapp.compose.widgets.HabitsListComponents
import com.example.habittrackerapp.compose.widgets.WeekDaysComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyHabitsScreen(
    viewModel: AppViewModel,
) {
    val sortedHabits by viewModel.sortedTodayHabits.collectAsStateWithLifecycle()
    val categoryMap by viewModel.categoryMap.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            NavTopBar(title = {
                Text(text = Routes.DailyHabits.route)
            }, canNavigateBack = false)
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            WeekDaysComponent()
            HabitsListComponents(sortedHabits, categoryMap, viewModel::onEvent)
        }
    }
}
