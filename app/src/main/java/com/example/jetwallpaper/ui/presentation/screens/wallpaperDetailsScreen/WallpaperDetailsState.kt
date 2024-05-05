package com.example.jetwallpaper.ui.presentation.screens.wallpaperDetailsScreen

data class WallpaperDetailsState(
    val savableWallpaper: SavableWallpaper = SavableWallpaper(),
    val isLoading:Boolean = false,
    val error:String = ""
    )