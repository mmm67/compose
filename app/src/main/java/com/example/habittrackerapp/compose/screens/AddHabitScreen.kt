package com.example.habittrackerapp.compose.screens


import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.habittrackerapp.CategoryEvent
import com.example.habittrackerapp.utilities.Constants
import com.example.habittrackerapp.HabitEvent
import com.example.habittrackerapp.viewmodels.AppViewModel
import com.example.habittrackerapp.OperationResult
import com.example.habittrackerapp.R
import com.example.habittrackerapp.ReminderEvent
import com.example.habittrackerapp.compose.NavTopBar
import com.example.habittrackerapp.compose.widgets.AutoResizedText
import com.example.habittrackerapp.extensions.navigateTo
import com.example.habittrackerapp.compose.widgets.ReminderComponent
import com.example.habittrackerapp.compose.menus.ScrollableDropdown
import com.example.habittrackerapp.compose.Routes
import com.example.habittrackerapp.compose.widgets.ReminderOption


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YesNoHabitScreen(
    navController: NavHostController,
    viewModel: AppViewModel,
    habitId: Int? = null
) {

    val categoryState = viewModel.categoryUiState.collectAsStateWithLifecycle()
    val habitState = viewModel.habitState.collectAsStateWithLifecycle()
    val reminderState = viewModel.reminderState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.result.collect { result ->
            when (result) {
                is OperationResult.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }

                OperationResult.Success -> {
                    navController.navigateTo(Routes.MyHabits.route)
                }
            }
        }
    }

    LaunchedEffect(true) {
        if (habitId == null) {
            viewModel.onEvent(CategoryEvent.ResetSelectedCategory)
            viewModel.onEvent(HabitEvent.ResetSelectedHabit)
            viewModel.onEvent(ReminderEvent.ResetSelectedReminder)
        }
    }

    Scaffold(topBar = {
        NavTopBar(
            title = {
                AutoResizedText(
                    text = "Add New Habit",
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            canNavigateBack = true,
            navigateUp = { navController.navigateTo(Routes.MyHabits.route) })
    },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(HabitEvent.SaveHabit)
                },
                containerColor = Color(0xff919bff),
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Add habit"
                )
            }
        }) { paddingValue ->
        Column(
            modifier = Modifier
                .padding(paddingValue)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = habitState.value.name,
                onValueChange = { viewModel.onEvent(HabitEvent.SetName(it)) },
                label = { Text("name") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xff919bff),
                    unfocusedBorderColor = Color(0xff919bff)
                ),
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.mandatory),
                        tint = Color.Red,
                        contentDescription = "mandatory"
                    )
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = habitState.value.description,
                onValueChange = { viewModel.onEvent(HabitEvent.SetDescription(it)) },
                label = { Text("description (optional)") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xff919bff),
                    unfocusedBorderColor = Color(0xff919bff)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            ScrollableDropdown(
                selectedItem = habitState.value.frequency,
                placeHolderTitle = "select frequency",
                items = listOf(
                    Constants.HabitFrequency.DAILY,
                    Constants.HabitFrequency.WEEKLY,
                    Constants.HabitFrequency.MONTHLY
                )
            ) {
                viewModel.onEvent(HabitEvent.SetFrequency(it))
            }

            Spacer(modifier = Modifier.height(8.dp))

            ScrollableDropdown(
                selectedItem = categoryState.value.selectedCategory.name,
                placeHolderTitle = "select category",
                items = categoryState.value.categoryListState.categories.map { it.name }.toList()
            ) {
                viewModel.onEvent(CategoryEvent.SetSelectedCategoryName(it))
            }

            Spacer(modifier = Modifier.height(8.dp))

            ReminderOption(habitState.value.isReminderEnabled) {
                viewModel.onEvent(HabitEvent.SetReminderEnabled(it))
            }

            if (habitState.value.isReminderEnabled) {
                ReminderComponent(reminderState.value, onEvent = {
                    viewModel.onEvent(it)
                }, frequency = habitState.value.frequency)
            }
        }
    }
}