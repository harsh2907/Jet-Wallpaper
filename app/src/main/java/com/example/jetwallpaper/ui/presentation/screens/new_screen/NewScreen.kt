package com.example.jetwallpaper.ui.presentation.screens.new_screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.jetwallpaper.ui.presentation.navigation.navigateToDetails
import com.example.jetwallpaper.ui.presentation.screens.search_screen.WallpaperItem
import com.example.jetwallpaper.ui.presentation.viewmodel.MainViewModel
import com.example.jetwallpaper.ui.presentation.viewmodel.UiEvent
import com.example.jetwallpaper.ui.theme.Violet
import com.example.jetwallpaper.ui.util.CustomLoading
import kotlinx.coroutines.launch


@Composable
fun NewScreen(
    viewModel: MainViewModel,
    navController: NavHostController
) {

    val wallpaperState = viewModel.newPager.collectAsLazyPagingItems()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val uiEvent = viewModel.uiEvent.collectAsState(initial = UiEvent.Idle).value


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp),
        scaffoldState = scaffoldState
    ) { padding ->

        when (uiEvent) {
            is UiEvent.ShowSnackBar -> {
                LaunchedEffect(key1 = true) {
                    scope.launch {
                        val action = scaffoldState.snackbarHostState.showSnackbar(
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
                    viewModel.sendUiEvent(
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
                        color = Violet
                    )
                }
            }
                is LoadState.Error -> {
                viewModel.sendUiEvent(
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
                    wallpaperState[index]?.let {
                        WallpaperItem(wallpaper = it, onClick = { wallpaper ->
                            navController.navigateToDetails(viewModel, wallpaper)
                        })
                    }
                }
            }

        }
    }
