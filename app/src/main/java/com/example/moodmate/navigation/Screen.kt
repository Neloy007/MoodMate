package com.example.moodmate.navigation

import com.example.moodmate.domain.model.Mood

sealed class Screen(val route: String) {
    data object Welcome : Screen("welcome")
    data object Home : Screen("home")
    data object Camera : Screen("camera")
    data object History : Screen("history")
    data object Analytics : Screen("analytics")
    data object Result : Screen("result?mood={mood}&smileProbability={smileProbability}") {
        fun passData(mood: Mood, smileProbability: Float): String {
            return "result?mood=${mood.name}&smileProbability=$smileProbability"
        }
    }
    data object Journal : Screen("journal/{moodId}") {
        fun passMoodId(moodId: Int): String = "journal/$moodId"
    }
    data object Settings : Screen("settings")
}