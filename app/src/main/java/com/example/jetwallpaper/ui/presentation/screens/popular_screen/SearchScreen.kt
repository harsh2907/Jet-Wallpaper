package com.example.jetwallpaper.ui.presentation.screens.popular_screen


import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.jetwallpaper.ui.presentation.navigation.navigateToDetails
import com.example.jetwallpaper.ui.presentation.screens.main.CustomSearchBar
import com.example.jetwallpaper.ui.presentation.screens.main.LoadingScreen
import com.example.jetwallpaper.ui.presentation.viewmodel.MainViewModel
import com.example.jetwallpaper.ui.presentation.viewmodel.UiAction
import com.example.jetwallpaper.ui.presentation.viewmodel.UiEvent
import com.example.jetwallpaper.ui.theme.Violet
import kotlinx.coroutines.launch


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchScreen(
    viewModel: MainViewModel,
    navController: NavHostController
) {

    val wallpaperState = viewModel.searchPager.collectAsLazyPagingItems()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val uiEvent = viewModel.uiEvent.collectAsState(initial = UiEvent.Idle).value


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp),
        scaffoldState = scaffoldState,
        topBar = {
            CustomSearchBar { query ->
                viewModel.updateSearchList(query,viewModel.accept)
            }
        }
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

        AnimatedContent(targetState = wallpaperState.loadState.refresh) { targetState ->
            when (targetState) {
                is LoadState.Loading -> {
                    LoadingScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is LoadState.Error -> {
                    viewModel.sendUiEvent(
                        UiEvent.ShowSnackBar(
                            message = "An Error occurred while loading content",
                            action = "Retry"
                        )
                    )
                }
                else ->  LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = padding
                ){

                    when (wallpaperState.loadState.append) {
                        is LoadState.Loading -> {
                            item{
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    CircularProgressIndicator(color = Violet, modifier = Modifier.size(30.dp))
                                }
                            }
                        }
                        is LoadState.Error -> {
                            viewModel.sendUiEvent(UiEvent.ShowSnackBar(
                                message = "An Error occurred while loading content",
                                action = "Retry"
                            ))
                        }
                        else -> Unit
                    }

                    items(wallpaperState.itemCount) { index ->
                        wallpaperState[index]?.let {
                            WallpaperItem(wallpaper = it, onClick = { wallpaper ->
                                navController.navigateToDetails(viewModel,wallpaper)
                            })
                        }
                    }
                }
            }
        }


    }
}