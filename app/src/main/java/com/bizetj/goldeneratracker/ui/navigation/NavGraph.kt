package com.bizetj.goldeneratracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bizetj.goldeneratracker.ui.screens.creation.CreationSeanceScreen
import com.bizetj.goldeneratracker.ui.screens.liste.ListeSeancesScreen
import com.bizetj.goldeneratracker.ui.screens.seance.SeanceEnCoursScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Liste.route
    ) {
        composable(Screen.Liste.route) {
            ListeSeancesScreen(
                onSeanceClick = { seanceId ->
                    navController.navigate("seance/$seanceId")
                }
            )
        }

        composable(Screen.Creation.route) {
            CreationSeanceScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "seance/{seanceId}",
            arguments = listOf(
                navArgument("seanceId") {
                    type = NavType.StringType
                }
            )
        ) {
            SeanceEnCoursScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Stats.route) {
            // Placeholder pour les stats (Milestone futur)
        }
    }
}