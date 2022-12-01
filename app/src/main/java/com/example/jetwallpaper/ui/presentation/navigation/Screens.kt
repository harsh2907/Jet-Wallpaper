package com.example.jetwallpaper.ui.presentation.navigation

sealed class Screens(val route:String) {
    object Search: Screens("Search")
    object New: Screens("New")
    object Favourite: Screens("Favourite")
    object Details: Screens("Details")
    object FullScreen: Screens("FullScreen")
}

