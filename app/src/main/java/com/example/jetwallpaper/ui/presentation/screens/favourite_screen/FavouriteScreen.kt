package com.example.jetwallpaper.ui.presentation.screens.favourite_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.jetwallpaper.ui.presentation.navigation.navigateToDetails
import com.example.jetwallpaper.ui.presentation.screens.search_screen.WallpaperItem
import com.example.jetwallpaper.ui.presentation.viewmodel.MainViewModel
import com.example.jetwallpaper.ui.theme.Typography
import com.example.jetwallpaper.ui.theme.Violet

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FavouriteScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val wallpaperState by viewModel.savedWallpapers.collectAsState()

    AnimatedContent(targetState = wallpaperState) { targetState ->
        when {
            targetState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Violet)
                }
            }

            targetState.error.isNotBlank() -> {
                //Handle Error
            }

            targetState.wallpapers.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No wallpapers found. Try saving them from other sections.",
                        style = Typography.h4
                    )
                }
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