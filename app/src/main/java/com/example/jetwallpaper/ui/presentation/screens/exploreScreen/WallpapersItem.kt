package com.example.jetwallpaper.ui.presentation.screens.exploreScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.ui.presentation.utils.LoadImage


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WallpaperItem(
    wallpaper: Wallpaper,
    onClick: (Wallpaper) -> Unit,
    onLongClick: (Wallpaper) -> Unit = {},
) {
   LoadImage(
       url = wallpaper.thumbnail,
       modifier = Modifier
           .combinedClickable(
               enabled = true,
               interactionSource = remember { MutableInteractionSource() },
               indication = LocalIndication.current,
               onClick = {
                   onClick(wallpaper)
               },
               onLongClick = {
                   onLongClick(wallpaper)
               }
           )
   )
}


