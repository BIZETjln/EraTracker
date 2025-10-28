package com.bizetj.goldeneratracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bizetj.goldeneratracker.ui.components.AppBottomBar
import com.bizetj.goldeneratracker.ui.navigation.NavGraph
import com.bizetj.goldeneratracker.ui.navigation.Screen
import com.bizetj.goldeneratracker.ui.theme.GoldenEraTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoldenEraTrackerTheme(darkTheme = false) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        // Cacher la BottomBar sur l'écran Welcome
                        if (currentRoute != Screen.Welcome.route) {
                            AppBottomBar(
                                navController = navController,
                                currentRoute = currentRoute
                            )
                        }
                    }
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        NavGraph(navController = navController)
                    }
                }
            }
        }
    }
}