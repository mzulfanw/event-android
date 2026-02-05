package com.example.event_dicoding.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Home : BottomNavItem("home", "Home", Icons.Filled.Home)
    data object Upcoming : BottomNavItem("upcoming", "Upcoming", Icons.Filled.DateRange)
    data object Finished : BottomNavItem("finished", "Finished", Icons.Filled.CheckCircle)
}
