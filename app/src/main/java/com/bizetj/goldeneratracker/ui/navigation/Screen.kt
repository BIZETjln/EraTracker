package com.bizetj.goldeneratracker.ui.navigation

sealed class Screen(val route: String) {
    object Liste : Screen("liste")
    object Creation : Screen("creation")
    object Stats : Screen("stats")
}