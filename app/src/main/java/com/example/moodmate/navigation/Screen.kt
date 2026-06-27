package com.example.moodmate.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Camera : Screen("camera")
    data object History : Screen("history")
    data object Analytics : Screen("analytics")
    data object Result : Screen("result")
}