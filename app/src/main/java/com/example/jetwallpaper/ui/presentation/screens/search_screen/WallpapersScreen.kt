package com.example.jetwallpaper.ui.presentation.screens.search_screen

import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.ui.presentation.utils.LoadImage


@Composable
fun WallpaperItem(
    wallpaper: Wallpaper,
    onClick: (Wallpaper) -> Unit
) {
   LoadImage(url = wallpaper.thumbnail, modifier = Modifier.clickable { onClick(wallpaper) })
}


