package com.bizetj.goldeneratracker.ui.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Liste : Screen("liste")
    object Creation : Screen("creation")
    object Stats : Screen("stats")
}