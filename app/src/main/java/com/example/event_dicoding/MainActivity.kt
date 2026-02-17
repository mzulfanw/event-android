package com.example.event_dicoding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.event_dicoding.data.local.SettingPreferences
import com.example.event_dicoding.data.local.dataStore
import com.example.event_dicoding.navigation.AppNavGraph
import com.example.event_dicoding.navigation.BottomNavItem
import com.example.event_dicoding.ui.setting.SettingViewModel
import com.example.event_dicoding.ui.setting.SettingViewModelFactory
import com.example.event_dicoding.ui.theme.EventdicodingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val pref = SettingPreferences.getInstance(dataStore)
            val settingViewModel: SettingViewModel = viewModel(
                factory = SettingViewModelFactory(pref)
            )
            val isDarkMode by settingViewModel.getThemeSettings().collectAsState(initial = false)

            EventdicodingTheme(darkTheme = isDarkMode) {
                App()
            }
        }
    }
}

@Composable
private fun App() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Upcoming,
        BottomNavItem.Finished,
        BottomNavItem.Favorite,
        BottomNavItem.Setting
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            val showBottomBar = items.any { it.route == currentRoute }
            if (showBottomBar) {
                NavigationBar {
                    items.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        )
    }
}
