package com.example.moodmate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moodmate.domain.model.Mood
import com.example.moodmate.presentation.analytics.AnalyticsScreen
import com.example.moodmate.presentation.camera.CameraScreen
import com.example.moodmate.presentation.history.HistoryScreen
import com.example.moodmate.presentation.home.HomeScreen
import com.example.moodmate.presentation.journal.JournalScreen
import com.example.moodmate.presentation.result.ResultScreen
import com.example.moodmate.presentation.settings.SettingsScreen
import com.example.moodmate.presentation.welcome.WelcomeScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onGetStarted = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onLogin = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

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
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(Screen.Camera.route) {
            CameraScreen(
                onSaved = { mood, smileProbability ->
                    // Navigate to Result with actual mood data
                    navController.navigate(
                        Screen.Result.passData(
                            mood = mood,
                            smileProbability = smileProbability
                        )
                    ) {
                        popUpTo(Screen.Camera.route) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen()
        }

        composable(Screen.Analytics.route) {
            AnalyticsScreen()
        }

        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument("mood") { type = NavType.StringType },
                navArgument("smileProbability") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val mood = backStackEntry.arguments?.getString("mood")?.let {
                try {
                    Mood.valueOf(it)
                } catch (e: IllegalArgumentException) {
                    Mood.HAPPY // Fallback
                }
            } ?: Mood.HAPPY

            val smileProbability = backStackEntry.arguments?.getFloat("smileProbability") ?: 0f

            ResultScreen(
                mood = mood,
                smileProbability = smileProbability,
                onDone = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Journal.route,
            arguments = listOf(
                navArgument("moodId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val moodId = backStackEntry.arguments?.getInt("moodId") ?: 0
            JournalScreen(
                moodId = moodId,
                onSave = {
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

