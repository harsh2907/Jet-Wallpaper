package com.example.jetwallpaper.ui.presentation.screens.favourite_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.ui.presentation.screens.exploreScreen.NoResultFound
import com.example.jetwallpaper.ui.presentation.screens.exploreScreen.WallpaperItem
import com.example.jetwallpaper.ui.presentation.screens.main.UiEvent
import com.example.jetwallpaper.ui.presentation.utils.WallpapersScreenState
import com.example.jetwallpaper.ui.util.CustomLoading


@Composable
fun FavouriteScreen(
    uiEvent: UiEvent,
    savedWallpaperState: WallpapersScreenState,
    navigateToDetails: (id: String) -> Unit,
    onEvent: (UiEvent) -> Unit,
    onLongClick: (Wallpaper) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = uiEvent) {
        when (uiEvent) {
            is UiEvent.Idle -> Unit

            is UiEvent.ShowSnackBar -> {
                snackbarHostState.showSnackbar(
                    message = uiEvent.message,
                    actionLabel = uiEvent.action,
                    duration = SnackbarDuration.Long
                )

            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { padding ->

        AnimatedContent(
            targetState = savedWallpaperState,
            label = "wallpaper",
            modifier = Modifier.fillMaxSize().padding(padding)
        ) { targetState ->

            when {
                targetState.isLoading -> {
                    CustomLoading()
                }

                targetState.error.isNotBlank() -> {
                    onEvent(
                        UiEvent.ShowSnackBar(
                            message = targetState.error,
                            action = "OK"
                        )
                    )
                }

                targetState.wallpapers.isEmpty() -> {
                    onEvent(
                        UiEvent.ShowSnackBar(
                            message = "No wallpapers found. Try saving them from other sections.",
                            action = "OK"
                        )
                    )
                    NoResultFound()
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2)
                    ) {
                        items(targetState.wallpapers) { wallpaper ->
                            WallpaperItem(
                                wallpaper = wallpaper,
                                onClick = { wp ->
                                    navigateToDetails(wp.id)
                                },
                                onLongClick = onLongClick
                            )
                        }
                    }
                }
            }
        }
    }
}

