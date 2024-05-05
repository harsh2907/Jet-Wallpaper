package com.example.jetwallpaper.ui.presentation.screens.newWallpapersScreen


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.jetwallpaper.data.utils.Constants
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.ui.presentation.screens.components.LazyWallpaperGrid
import com.example.jetwallpaper.ui.presentation.screens.main.UiEvent
import com.example.jetwallpaper.ui.theme.UiColors
import com.example.jetwallpaper.ui.util.CustomLoading


@Composable
fun NewWallpaperScreen(
    uiEvent: UiEvent,
    wallpaperState: LazyPagingItems<Wallpaper>,
    selectedSortingParam: String,
    onSortingParamChange: (String) -> Unit,
    navigateToDetails: (id: String) -> Unit,
    onEvent: (UiEvent) -> Unit
) {

    val lazyGridState = rememberLazyGridState()
    val snackbarHostState = remember { SnackbarHostState() }

    val isOnTop by remember {
        derivedStateOf {
            lazyGridState.firstVisibleItemIndex < 2
        }
    }

    LaunchedEffect(key1 = uiEvent) {
        when (uiEvent) {
            is UiEvent.ShowSnackBar -> {
                val action = snackbarHostState.showSnackbar(
                    message = uiEvent.message,
                    actionLabel = uiEvent.action,
                    duration = SnackbarDuration.Long
                )
                if (action == SnackbarResult.ActionPerformed) {
                    wallpaperState.retry()
                }
            }

            is UiEvent.Idle -> Unit

        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
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

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            LazyWallpaperGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .align(Alignment.Center),
                state = lazyGridState,
                wallpaperState = wallpaperState,
                onClick = navigateToDetails,
                onEvent = onEvent
            )

            AnimatedVisibility(
                visible = isOnTop,
                enter = slideInVertically { -it },
                exit = slideOutVertically { -it }
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 8.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = Constants.SortingParams.entries
                    ) { param ->
                        FilterChip(
                            selected = param.paramName == selectedSortingParam,
                            onClick = { onSortingParamChange(param.paramName) },
                            label = { Text(text = param.getGeneralizedName()) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = UiColors.DeepPink,
                                labelColor = UiColors.Violet,
                                selectedLabelColor = Color.White
                            ),
                            shape = CircleShape,
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedSortingParam == param.paramName,
                                selectedBorderColor = UiColors.HotPink,
                                borderColor = UiColors.Violet
                            )
                        )
                    }
                }
            }
        }
    }
}
