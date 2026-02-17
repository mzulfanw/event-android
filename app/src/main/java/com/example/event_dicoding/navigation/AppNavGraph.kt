package com.example.event_dicoding.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.event_dicoding.screens.DetailScreen
import com.example.event_dicoding.screens.FavoriteScreen
import com.example.event_dicoding.screens.HomeScreen
import com.example.event_dicoding.screens.SettingScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(
                currentRoute = BottomNavItem.Home.route,
                navigateToDetail = { eventId ->
                    navController.navigate("detail/$eventId")
                }
            )
        }
        composable(BottomNavItem.Upcoming.route) {
            HomeScreen(
                currentRoute = BottomNavItem.Upcoming.route,
                navigateToDetail = { eventId ->
                    navController.navigate("detail/$eventId")
                }
            )
        }
        composable(BottomNavItem.Finished.route) {
            HomeScreen(
                currentRoute = BottomNavItem.Finished.route,
                navigateToDetail = { eventId ->
                    navController.navigate("detail/$eventId")
                }
            )
        }
        composable(BottomNavItem.Favorite.route) {
            FavoriteScreen(
                navigateToDetail = { eventId ->
                    navController.navigate("detail/$eventId")
                }
            )
        }
        composable(BottomNavItem.Setting.route) {
            SettingScreen()
        }
        composable(
            route = "detail/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.IntType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: return@composable
            DetailScreen(
                eventId = eventId,
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}
