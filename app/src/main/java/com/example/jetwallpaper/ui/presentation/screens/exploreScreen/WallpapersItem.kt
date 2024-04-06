package com.example.jetwallpaper.ui.presentation.screens.exploreScreen

import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import coil.annotation.ExperimentalCoilApi
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.ui.presentation.utils.LoadImage


@OptIn(ExperimentalCoilApi::class)
@Composable
fun WallpaperItem(
    wallpaper: Wallpaper,
    onClick: (Wallpaper) -> Unit
) {
   LoadImage(
       url = wallpaper.thumbnail,
       modifier = Modifier.clickable { onClick(wallpaper) }
   )
}


