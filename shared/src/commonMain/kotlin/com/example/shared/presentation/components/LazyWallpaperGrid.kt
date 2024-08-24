package com.example.shared.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.shared.domain.models.WallpaperItem
import com.example.shared.presentation.WallpaperState
import com.example.shared.presentation.util.UiColors
import com.example.shared.presentation.util.UiEvent

@Composable
fun LazyWallpaperGrid(
    modifier: Modifier = Modifier,
    columns: GridCells = GridCells.Fixed(2),
    wallpaperState: WallpaperState,
    onClick: (WallpaperItem) -> Unit,
    onEvent: (UiEvent) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = columns
    ) {
        items(wallpaperState.wallpapers) { wallpaper ->
            WallpaperItem(
                wallpaper = wallpaper,
                onClick = {
                    onClick(it)
                }
            )
        }

        when  {
            wallpaperState.isLoading -> {
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

            wallpaperState.error.isNotEmpty() -> {
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