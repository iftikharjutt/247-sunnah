package com.sunnah.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sunnah.app.ui.navigation.Screen
import com.sunnah.app.ui.navigation.bottomNavItems
import com.sunnah.app.ui.theme.SunnahAppTheme
import com.sunnah.app.viewmodel.MainViewModel
import com.sunnah.app.ui.today.TodayScreen
import com.sunnah.app.ui.dhikr.DhikrScreen
import com.sunnah.app.ui.ai.AIGuideScreen
import com.sunnah.app.ui.settings.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SunnahAppTheme {
                val viewModel: MainViewModel = viewModel()
                val navController = rememberNavController()
                
                Scaffold(
                    modifier = Modifier.testTag("app_scaffold"),
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier.testTag("bottom_navigation_bar"),
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route
                            
                            bottomNavItems.forEach { screen ->
                                NavigationBarItem(
                                    icon = { Icon(screen.icon, contentDescription = screen.title) },
                                    label = { Text(screen.title) },
                                    selected = currentRoute == screen.route,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Today.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Today.route) { TodayScreen(viewModel) }
                        composable(Screen.Dhikr.route) { DhikrScreen(viewModel) }
                        composable(Screen.AIGuide.route) { AIGuideScreen(viewModel) }
                        composable(Screen.Settings.route) { SettingsScreen(viewModel) }
                    }
                }
            }
        }
    }
}
