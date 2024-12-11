package com.example.habittrackerapp.compose.widgets

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.habittrackerapp.extensions.navigateTo
import com.example.habittrackerapp.compose.BottomNavigationItem

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val pages = listOf(
        BottomNavigationItem.MyHabits,
        BottomNavigationItem.DailyHabits,
        BottomNavigationItem.Categories
    )

    NavigationBar(
        modifier = modifier
    ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        pages.forEach { page ->
            NavigationBarItem(
                alwaysShowLabel = true,
                selected = currentRoute == page.route,
                label = {
                    AutoResizedText(text = page.label)
                },
                icon = {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = page.icon),
                        contentDescription = page.label,
                        tint = Color.Unspecified
                    )
                },
                onClick = {
                    navController.navigateTo(page.route)
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = Color.Gray,
                    selectedTextColor = Color.Black,
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Black,
                    indicatorColor =  Color(0xff919bff)
                ),
            )
        }
    }
}