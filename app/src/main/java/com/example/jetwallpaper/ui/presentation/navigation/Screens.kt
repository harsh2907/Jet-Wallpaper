package com.example.jetwallpaper.ui.presentation.navigation

import androidx.navigation.NavHostController
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.ui.presentation.viewmodel.MainViewModel

sealed class Screens(val route:String) {
    object Popular: Screens("Popular")
    object New: Screens("New")
    object Favourite: Screens("Favourite")
    object Details: Screens("Details")
    object FullScreen: Screens("FullScreen")
}

