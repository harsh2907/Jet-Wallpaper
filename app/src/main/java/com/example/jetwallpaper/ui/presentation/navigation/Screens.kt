package com.example.jetwallpaper.ui.presentation.navigation

sealed class Screens(val route:String) {
    data object Search: Screens("Search")
    data object New: Screens("New")
    data object Favourite: Screens("Favourite")
    data object Details: Screens("Details")
    data object FullScreen: Screens("FullScreen")
}

