package com.example.habittrackerapp

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.habittrackerapp.compose.screens.CategoriesScreen
import com.example.habittrackerapp.compose.screens.YesNoHabitScreen
import com.example.habittrackerapp.compose.Routes
import com.example.habittrackerapp.compose.screens.DailyHabitsScreen
import com.example.habittrackerapp.compose.screens.MyHabitsScreen
import com.example.habittrackerapp.viewmodels.AppViewModel

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onBottomBarVisibilityChanged: (Boolean) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Routes.MyHabits.route
    ) {
        composable(
            Routes.DailyHabits.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }) {
            onBottomBarVisibilityChanged(true)
            DailyHabitsScreen(
                viewModel = hiltViewModel<AppViewModel>()
            )
        }

        composable(
            Routes.AddHabit.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }) {
            onBottomBarVisibilityChanged(false)
            YesNoHabitScreen(navController, hiltViewModel<AppViewModel>())
        }

        composable(
            Routes.Categories.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }) {
            onBottomBarVisibilityChanged(true)
            CategoriesScreen(hiltViewModel<AppViewModel>())
        }

        composable(
            Routes.MyHabits.route,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }) {
            onBottomBarVisibilityChanged(true)
            MyHabitsScreen(navController = navController, hiltViewModel<AppViewModel>())
        }
    }
}