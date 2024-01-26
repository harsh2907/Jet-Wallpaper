package com.example.jetwallpaper.ui.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.jetwallpaper.ui.presentation.screens.details_screen.DetailsScreen
import com.example.jetwallpaper.ui.presentation.screens.details_screen.FullScreen
import com.example.jetwallpaper.ui.presentation.screens.favourite_screen.FavouriteScreen
import com.example.jetwallpaper.ui.presentation.screens.new_screen.NewScreen
import com.example.jetwallpaper.ui.presentation.screens.search_screen.SearchScreen
import com.example.jetwallpaper.ui.presentation.viewmodel.MainViewModel
import com.example.jetwallpaper.ui.presentation.viewmodel.UiEvent

@Composable
fun JetWallpaperNavScreenContent(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val viewModel: MainViewModel = hiltViewModel()
    val wallpaperState = viewModel.searchPager.collectAsLazyPagingItems()
    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = UiEvent.Idle)

    NavHost(
        navController = navController,
        startDestination = Screens.New.route,
        modifier = modifier
    ) {

        composable(route = Screens.Search.route) {

            SearchScreen(
                wallpaperState = wallpaperState,
                uiEvent = uiEvent,
                onEvent = { event ->
                    viewModel.sendUiEvent(event)
                },
                updateSearchList = { query ->
                    viewModel.updateSearchList(query, viewModel.accept)
                },
                navigateToDetails = { wallpaper ->
                    viewModel.currentWallpaper = wallpaper
                    navController.navigateToDetails()
                }
            )

        }

        composable(route = Screens.New.route) {

            NewScreen(
                uiEvent = uiEvent,
                wallpaperState = wallpaperState,
                onEvent = { event ->
                    viewModel.sendUiEvent(event)
                },
                navigateToDetails = { wallpaper ->
                    viewModel.currentWallpaper = wallpaper
                    navController.navigateToDetails()
                }
            )

        }
        composable(route = Screens.Favourite.route) {

            val savedWallpaperState by viewModel.savedWallpapers
                .collectAsStateWithLifecycle()

            FavouriteScreen(
                uiEvent = uiEvent,
                savedWallpaperState = savedWallpaperState,
                onEvent = viewModel::sendUiEvent,
                navigateToDetails = { wallpaper ->
                    viewModel.currentWallpaper = wallpaper
                    navController.navigateToDetails()
                })

        }

        composable(route = Screens.Details.route) {

            DetailsScreen(
                uiEvent = uiEvent,
                currentWallpaper = viewModel.currentWallpaper,
                saveWallpaper = viewModel::addWallpaper,
                navigateToFullScreen = { _ ->
                    navController.navigate(Screens.FullScreen.route)
                },
                onEvent = viewModel::sendUiEvent
            )

        }

        composable(route = Screens.FullScreen.route,) {
            FullScreen(imageUrl = viewModel.currentWallpaper?.imageUrl)
        }

    }
}

fun NavHostController.navigateToDetails() {

    navigate(Screens.Details.route) {
        launchSingleTop = true
    }
}