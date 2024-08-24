package com.example.shared.presentation


import com.example.shared.domain.models.WallpaperItem

data class WallpaperState(
    val wallpapers:List<WallpaperItem> = emptyList(),
    val isLoading: Boolean = false,
    val error:String = ""
)