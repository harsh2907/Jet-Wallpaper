package com.example.jetwallpaper.ui.presentation.screens.new_screen

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.jetwallpaper.ui.presentation.navigation.navigateToDetails
import com.example.jetwallpaper.ui.presentation.screens.main.LoadingScreen
import com.example.jetwallpaper.ui.presentation.screens.popular_screen.WallpaperItem
import com.example.jetwallpaper.ui.presentation.viewmodel.MainViewModel
import com.example.jetwallpaper.ui.theme.Pink
import com.example.jetwallpaper.ui.theme.Violet
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NewScreen(
    viewModel: MainViewModel,
    navController: NavHostController
) {

    val wallpaperState = viewModel.newPager.collectAsLazyPagingItems()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp),
        scaffoldState = scaffoldState
    ) { padding ->

        when (wallpaperState.loadState.refresh) {
            is LoadState.Loading -> {
                LoadingScreen(
                    modifier = Modifier.fillMaxSize()
                )
            }
            is LoadState.Error -> {
                Toast.makeText(
                    context,
                    "An Error occurred while loading content",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("NewScreen","Error in popular screen")

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
                Toast.makeText(
                    context,
                    "An Error occurred while loading content",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("NewScreen","Error in popular screen")

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