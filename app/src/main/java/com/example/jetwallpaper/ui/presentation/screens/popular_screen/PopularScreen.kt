package com.example.jetwallpaper.ui.presentation.screens.popular_screen

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
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.jetwallpaper.ui.presentation.navigation.navigateToDetails
import com.example.jetwallpaper.ui.presentation.screens.main.CustomSearchBar
import com.example.jetwallpaper.ui.presentation.screens.main.LoadingScreen
import com.example.jetwallpaper.ui.presentation.viewmodel.MainViewModel
import com.example.jetwallpaper.ui.theme.Violet
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PopularScreen(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    val wallpaperState = viewModel.popularPager.collectAsLazyPagingItems()

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp),
        scaffoldState = scaffoldState,
        topBar = {
            CustomSearchBar(viewModel = viewModel){

            }
        }
    ) { padding ->

        when (wallpaperState.loadState.refresh) {
            is LoadState.Loading -> {
                    LoadingScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            is LoadState.Error -> {
                Log.e("PopularScreen","Error in popular screen")
                Toast.makeText(context, "An Error occurred while loading content", Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }

        LazyVerticalGrid(
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
                    Toast.makeText(context, "An Error occurred while loading content", Toast.LENGTH_SHORT).show()
                    Log.e("PopularScreen","Error in popular screen")
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