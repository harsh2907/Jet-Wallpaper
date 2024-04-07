package com.example.jetwallpaper.ui.presentation.screens.wallpaperDetailsScreen

import com.example.jetwallpaper.domain.models.Wallpaper

data class WallpaperDetailsState(
    val wallpaper: Wallpaper? = null,
    val isLoading:Boolean = false,
    val error:String = ""
    )