package com.example.jetwallpaper.ui.presentation.screens.favourite_screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jetwallpaper.ui.presentation.navigation.navigateToDetails
import com.example.jetwallpaper.ui.presentation.screens.search_screen.NoResultFound
import com.example.jetwallpaper.ui.presentation.screens.search_screen.WallpaperItem
import com.example.jetwallpaper.ui.presentation.viewmodel.MainViewModel
import com.example.jetwallpaper.ui.presentation.viewmodel.UiEvent
import com.example.jetwallpaper.ui.util.CustomLoading
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FavouriteScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val wallpaperState by viewModel.savedWallpapers.collectAsState()
    val uiEvent = viewModel.uiEvent.collectAsState(initial = UiEvent.Idle).value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp),
        scaffoldState = scaffoldState
    ){
        when (uiEvent) {
            is UiEvent.Idle -> Unit

            is UiEvent.ShowSnackBar -> {
                LaunchedEffect(key1 = true) {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = uiEvent.message,
                            actionLabel = uiEvent.action,
                            duration = SnackbarDuration.Long
                        )
                    }
                }
            }
        }
        
        AnimatedContent(targetState = wallpaperState) { targetState ->

            when {
                targetState.isLoading -> {
                   CustomLoading()
                }

                targetState.error.isNotBlank() -> {
                    viewModel.sendUiEvent(UiEvent.ShowSnackBar(
                        message = targetState.error,
                        action = "OK"
                    ))
                }

                targetState.wallpapers.isEmpty() -> {
                    viewModel.sendUiEvent(UiEvent.ShowSnackBar(
                        message =  "No wallpapers found. Try saving them from other sections.",
                        action = "OK"
                    ))
                    NoResultFound()
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2)
                    ) {
                        items(targetState.wallpapers) { wallpaper ->
                            WallpaperItem(wallpaper = wallpaper, onClick = { wp ->
                                navController.navigateToDetails(viewModel, wp)
                            })
                        }
                    }
                }
            }
        }
    }
}

