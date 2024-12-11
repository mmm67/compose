package com.example.habittrackerapp.compose

sealed class Routes(val route: String) {
    data object DailyHabits : Routes("Daily Habits")
    data object AddHabit : Routes("AddHabit")
    data object Categories : Routes("Categories")
    data object MyHabits : Routes("MyHabits")
}