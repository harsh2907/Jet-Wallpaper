package com.example.jetwallpaper.ui.presentation.screens.wallpaperDetailsScreen

import com.example.jetwallpaper.domain.models.Wallpaper

data class SavableWallpaper(
    val wallpaper:Wallpaper? = null,
    val isFavourite: Boolean = false
)