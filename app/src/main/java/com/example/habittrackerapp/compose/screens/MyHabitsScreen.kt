package com.example.habittrackerapp.compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.habittrackerapp.viewmodels.AppViewModel
import com.example.habittrackerapp.R
import com.example.habittrackerapp.compose.NavTopBar
import com.example.habittrackerapp.extensions.navigateTo
import com.example.habittrackerapp.compose.Routes
import com.example.habittrackerapp.compose.widgets.HabitsListComponents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyHabitsScreen(
    navController: NavHostController,
    viewModel: AppViewModel,
) {
    val habitListUiState by viewModel.habitListUiState.collectAsStateWithLifecycle()
    val categoryMap by viewModel.categoryMap.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            NavTopBar(title = {
                Text(text = Routes.MyHabits.route)
            }, canNavigateBack = false)
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigateTo(Routes.AddHabit.route)
                },
                containerColor = Color(0xff919bff),
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                shape = CircleShape
            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add habit"
                )
            }
        }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){
            if (habitListUiState.habits.isEmpty()) {
                NoHabitsScreen()
            } else {
                HabitsListComponents(habitListUiState.habits, categoryMap, viewModel::onEvent)
            }
        }
    }
}

@Composable
fun NoHabitsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Illustration or Icon
        Icon(
            painter = painterResource(id = R.drawable.lifestyle),
            contentDescription = "No Habits",
            modifier = Modifier.size(100.dp),
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Message
        Text(
            text = "No habits yet! Start building your habits to achieve your goals.",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}