package com.example.jetwallpaper.navigation

sealed class Screens(val route:String) {
    data object Explore: Screens("Explore")
    data object New: Screens("New")

    data object WallpaperDetails:Screens("WallpaperDetails"){
        data object Details: Screens("Details/{wallpaperId}"){
            fun passWallpaperId(wallpaperId:String) = "Details/${wallpaperId}"
        }
        data object FullScreen: Screens("FullScreen")
    }

    data object Favourite: Screens("Favourite")

}


