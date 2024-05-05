package com.example.jetwallpaper.ui.presentation.screens.exploreScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.jetwallpaper.R
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.ui.presentation.screens.components.CustomSearchBar
import com.example.jetwallpaper.ui.presentation.screens.components.LazyWallpaperGrid
import com.example.jetwallpaper.ui.presentation.screens.main.UiEvent
import com.example.jetwallpaper.ui.util.CustomLoading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    wallpaperState: LazyPagingItems<Wallpaper>,
    uiEvent: UiEvent,
    onEvent: (UiEvent) -> Unit,
    updateSearchList: (String) -> Unit,
    navigateToDetails: (id: String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val lazyGridState = rememberLazyGridState()

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

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val isTop by remember {
        derivedStateOf {
            lazyGridState.firstVisibleItemIndex == 0
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
//            AnimatedVisibility(
//                visible = isTop,
//                enter = slideInVertically(animationSpec = tween()) { it },
//                exit = slideOutVertically(animationSpec = tween()) { -it }
//            ) {
//                CustomSearchBar(onSearch = updateSearchList)
//            }
//
//            TopAppBar(
//                title = {
//                    CustomSearchBar(onSearch = updateSearchList)
//                },
//                scrollBehavior = scrollBehavior,
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color.Transparent ,
//                    scrolledContainerColor = Color.Transparent
//                )
//            )

        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
                AnimatedContent(
                targetState = wallpaperState.loadState.refresh,
                label = "SearchScreen"
            ) { targetState ->
                when (targetState) {
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

                    else -> {
                        AnimatedVisibility(visible = wallpaperState.itemCount == 0) {
                            NoResultFound()
                        }


                        AnimatedVisibility(visible = wallpaperState.itemCount > 0) {
                            Column {
                                CustomSearchBar(onSearch = updateSearchList)

                                LazyWallpaperGrid(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(
                                            bottom = padding.calculateBottomPadding()
                                        ),
                                    wallpaperState = wallpaperState,
                                    onClick = navigateToDetails,
                                    onEvent = onEvent,
                                    state = lazyGridState
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun NoResultFound() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_data_found),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}