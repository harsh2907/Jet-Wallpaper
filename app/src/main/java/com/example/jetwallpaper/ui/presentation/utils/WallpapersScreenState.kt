package com.example.jetwallpaper.ui.presentation.utils

import com.example.jetwallpaper.domain.models.Wallpaper

data class WallpapersScreenState(
    val wallpapers:List<Wallpaper> = emptyList(),
    val isLoading:Boolean = true,
    val error:String = ""
)
