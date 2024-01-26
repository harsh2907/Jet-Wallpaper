package com.example.jetwallpaper.ui.presentation.screens.search_screen


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.jetwallpaper.R
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.ui.presentation.navigation.navigateToDetails
import com.example.jetwallpaper.ui.presentation.screens.main.CustomSearchBar
import com.example.jetwallpaper.ui.presentation.viewmodel.MainViewModel
import com.example.jetwallpaper.ui.presentation.viewmodel.UiAction
import com.example.jetwallpaper.ui.presentation.viewmodel.UiEvent
import com.example.jetwallpaper.ui.theme.Violet
import com.example.jetwallpaper.ui.util.CustomLoading
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    wallpaperState: LazyPagingItems<Wallpaper>,
    uiEvent: UiEvent,
    onEvent:(UiEvent)->Unit,
    updateSearchList:(String)->Unit,
    navigateToDetails:(Wallpaper)->Unit
) {

    val scope = rememberCoroutineScope()
    val keyboard = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier
            .fillMaxSize() ,
        topBar = {
            CustomSearchBar { query ->
                 updateSearchList(query)
                keyboard?.hide()
            }
        }
    ) { padding ->

        when (uiEvent) {
            is UiEvent.ShowSnackBar -> {
                LaunchedEffect(key1 = true) {
                    scope.launch {
                        val action =  snackbarHostState.showSnackbar(
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

        AnimatedContent(
            targetState = wallpaperState.loadState.refresh,
            label = ""
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
                    AnimatedVisibility(visible = wallpaperState.itemCount != 0) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = padding
                        ) {
                            when (wallpaperState.loadState.append) {
                                is LoadState.Loading -> {
                                    item {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                           CircularProgressIndicator(color = Violet)
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
                            /*
                            if (wallpaperState.itemCount == 0) {
                                viewModel.sendUiEvent(
                                    UiEvent.ShowSnackBar(
                                        message = "Sorry. No result found.",
                                        action = "Close"
                                    )
                                )
                            }
                             */
                            items(wallpaperState.itemCount) { index ->
                                wallpaperState[index]?.let {
                                    WallpaperItem(wallpaper = it, onClick = { wallpaper ->
                                        navigateToDetails(wallpaper)
                                    })
                                }
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