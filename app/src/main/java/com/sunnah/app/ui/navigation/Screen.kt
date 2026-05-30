package com.sunnah.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Today : Screen("today", "Today", Icons.Default.Today)
    object Dhikr : Screen("dhikr", "Dhikr", Icons.Default.AddCircle)
    object AIGuide : Screen("ai_guide", "AI Guide", Icons.Default.Chat)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

val bottomNavItems = listOf(
    Screen.Today,
    Screen.Dhikr,
    Screen.AIGuide,
    Screen.Settings
)
