package com.example.jetwallpaper.navigation

sealed class Screens(val route:String) {
    data object Search: Screens("Search")
    data object New: Screens("New")
    data object Favourite: Screens("Favourite")
    data object Details: Screens("Details/{wallpaperId}"){
        fun passWallpaperId(wallpaperId:String) = "Details/${wallpaperId}"
    }
    data object FullScreen: Screens("FullScreen")
}

