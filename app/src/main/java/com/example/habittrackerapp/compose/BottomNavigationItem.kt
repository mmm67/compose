package com.example.habittrackerapp.compose

import com.example.habittrackerapp.R

sealed class BottomNavigationItem(
    val label: String,
    val icon: Int,
    val route: String
) {

    data object DailyHabits : BottomNavigationItem(
        label = "Daily Habits",
        icon = R.drawable.dailyhabit,
        route = Routes.DailyHabits.route
    )

    data object MyHabits : BottomNavigationItem(
        label = "My Habits",
        icon = R.drawable.habit,
        route = Routes.MyHabits.route
    )

    data object Categories : BottomNavigationItem(
        label = "Categories",
        icon = R.drawable.category,
        route = Routes.Categories.route
    )
}