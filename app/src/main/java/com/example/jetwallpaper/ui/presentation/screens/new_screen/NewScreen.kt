package com.example.jetwallpaper.ui.presentation.screens.new_screen


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.ui.presentation.screens.search_screen.WallpaperItem
import com.example.jetwallpaper.ui.presentation.viewmodel.UiEvent
import com.example.jetwallpaper.ui.theme.UiColors
import com.example.jetwallpaper.ui.util.CustomLoading
import kotlinx.coroutines.launch


@Composable
fun NewScreen(
    uiEvent: UiEvent,
    wallpaperState: LazyPagingItems<Wallpaper>,
    navigateToDetails: (Wallpaper) -> Unit,
    onEvent: (UiEvent) -> Unit
) {


    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { padding ->

        when (uiEvent) {
            is UiEvent.ShowSnackBar -> {
                LaunchedEffect(key1 = true) {
                    scope.launch {
                        val action = snackbarHostState.showSnackbar(
                            message = uiEvent.message,
                            actionLabel = uiEvent.action,
                            duration = SnackbarDuration.Long
                        )
                        if (action == SnackbarResult.ActionPerformed) {
                            wallpaperState.retry()
                        }
                    }
                }
            }

            is UiEvent.Idle -> Unit
        }

        when (wallpaperState.loadState.refresh) {
            is LoadState.Loading -> {
                CustomLoading()
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

        when (wallpaperState.loadState.append) {

            is LoadState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    CircularProgressIndicator(
                        color = UiColors.Violet
                    )
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


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(padding)
        ) {

            items(wallpaperState.itemCount) { index ->
                wallpaperState[index]?.let { wallpaper ->

                    WallpaperItem(
                        wallpaper = wallpaper,
                        onClick = { item: Wallpaper ->
                            navigateToDetails(item)
                        })
                }
            }
        }

    }
}
