package com.example.moodmate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moodmate.presentation.analytics.AnalyticsScreen
import com.example.moodmate.presentation.camera.CameraScreen
import com.example.moodmate.presentation.history.HistoryScreen
import com.example.moodmate.presentation.home.HomeScreen
import com.example.moodmate.presentation.result.ResultScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ){
        composable(Screen.Home.route) {
            HomeScreen(
                onCheckMoodClick = {
                    navController.navigate(Screen.Camera.route)
                },
                onHistoryClick = {
                    navController.navigate(Screen.History.route)
                },
                onAnalyticsClick = {
                    navController.navigate(Screen.Analytics.route)
                }
            )
        }

        composable(Screen.Camera.route) {
            CameraScreen()
        }

        composable(Screen.History.route) {
            HistoryScreen()
        }

        composable(Screen.Analytics.route) {
            AnalyticsScreen()
        }

        composable(Screen.Result.route) {
            ResultScreen()
        }

    }


}