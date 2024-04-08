package com.example.jetwallpaper.ui.presentation.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.ui.presentation.screens.exploreScreen.WallpaperItem
import com.example.jetwallpaper.ui.presentation.screens.main.UiEvent
import com.example.jetwallpaper.ui.theme.UiColors

@Composable
fun LazyWallpaperGrid(
    modifier:Modifier = Modifier,
    columns:GridCells = GridCells.Fixed(2),
    wallpaperState:LazyPagingItems<Wallpaper>,
    onClick:(wallpaperId:String)->Unit,
    onEvent: (UiEvent)->Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = columns
    ) {
        items(wallpaperState.itemCount) { index ->
            wallpaperState[index]?.let {
                WallpaperItem(wallpaper = it, onClick = { wallpaper ->
                    onClick(wallpaper.id)
                })
            }
        }

        when (wallpaperState.loadState.append) {
            is LoadState.Loading -> {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(color = UiColors.Violet)
                    }
                }
            }

            is LoadState.Error -> {
                onEvent(
                    UiEvent.ShowSnackBar(
                        message = "An Error occurred while loading content",
                        action = "Retry"
                    )
                )
            }

            else -> Unit
        }
    }
}