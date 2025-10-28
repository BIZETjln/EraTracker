package com.bizetj.goldeneratracker.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.navigation.NavController
import com.bizetj.goldeneratracker.ui.navigation.Screen

@Composable
fun AppBottomBar(
    navController: NavController,
    currentRoute: String?
) {
    NavigationBar {
        val items = listOf(
            BottomNavItem("Liste", Icons.AutoMirrored.Filled.List, Screen.Liste.route),
            BottomNavItem("Créer", Icons.Default.Add, Screen.Creation.route),
            BottomNavItem("Stats", Icons.Default.Star, Screen.Stats.route)
        )

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(Screen.Liste.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)