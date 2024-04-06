package com.example.jetwallpaper.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.jetwallpaper.ui.presentation.screens.wallpaperDetailsScreen.DetailsScreen
import com.example.jetwallpaper.ui.presentation.screens.wallpaperDetailsScreen.FullScreen
import com.example.jetwallpaper.ui.presentation.screens.exploreScreen.SearchScreen
import com.example.jetwallpaper.ui.presentation.screens.favourite_screen.FavouriteScreen
import com.example.jetwallpaper.ui.presentation.screens.newWallpapersScreen.NewWallpaperScreen
import com.example.jetwallpaper.ui.presentation.screens.newWallpapersScreen.NewWallpapersViewModel
import com.example.jetwallpaper.ui.presentation.screens.wallpaperDetailsScreen.WallpaperDetailsViewModel
import com.example.jetwallpaper.ui.presentation.screens.main.MainViewModel
import com.example.jetwallpaper.ui.presentation.screens.main.UiEvent

@Composable
fun JetWallpaperNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val viewModel: MainViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screens.New.route,
        modifier = modifier,
        enterTransition = { slideInHorizontally { it } + fadeIn() },
        exitTransition = { slideOutHorizontally { -it } + fadeOut() },
        popEnterTransition = { slideInHorizontally { -it } + fadeIn() },
        popExitTransition = { slideOutHorizontally { it } + fadeOut() },
    ) {

        composable(route = Screens.Search.route) {

            val wallpaperState = viewModel.searchPager.collectAsLazyPagingItems()
            val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = UiEvent.Idle)

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
                    navController.navigateToDetails(wallpaperId = wallpaper.id)
                }
            )

        }

        composable(route = Screens.New.route) {

            val newWallpapersViewModel: NewWallpapersViewModel = hiltViewModel()

            val uiEvent by
                newWallpapersViewModel.uiEvent.collectAsStateWithLifecycle(initialValue = UiEvent.Idle)

            val wallpaperState = newWallpapersViewModel.wallpaperPager.collectAsLazyPagingItems()

            NewWallpaperScreen(
                uiEvent = uiEvent,
                wallpaperState = wallpaperState,
                onEvent = { event ->
                    newWallpapersViewModel.sendUiEvent(event)
                },
                navigateToDetails = { wallpaper ->
                    viewModel.currentWallpaper = wallpaper
                    navController.navigateToDetails(wallpaperId = wallpaper.id)
                }
            )

        }
        composable(route = Screens.Favourite.route) {
            val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle(initialValue = UiEvent.Idle)

            val savedWallpaperState by viewModel.savedWallpapers
                .collectAsStateWithLifecycle()

            FavouriteScreen(
                uiEvent = uiEvent,
                savedWallpaperState = savedWallpaperState,
                onEvent = viewModel::sendUiEvent,
                navigateToDetails = { wallpaper ->
                    viewModel.currentWallpaper = wallpaper
                    navController.navigateToDetails(wallpaperId = wallpaper.id)
                }
            )

        }

        composable(
            route = Screens.Details.route,
            arguments = listOf(
                navArgument("wallpaperId"){
                    type = NavType.StringType
                }
            )
        ) {
            val detailsViewModel:WallpaperDetailsViewModel = hiltViewModel()

            val uiEvent by detailsViewModel.uiEvent.collectAsStateWithLifecycle(initialValue = UiEvent.Idle)
            val wallpaperDetailsState by detailsViewModel.wallpaperDetailsState.collectAsStateWithLifecycle()

            DetailsScreen(
                uiEvent = uiEvent,
                wallpaperDetailsState = wallpaperDetailsState,
                saveWallpaper = viewModel::addWallpaper,
                navigateToFullScreen = { _ ->
                    navController.navigate(Screens.FullScreen.route)
                },
                onEvent = detailsViewModel::sendUiEvent,
                reloadWallpaper = detailsViewModel::reloadWallpaper
            )

        }

        composable(route = Screens.FullScreen.route) {
            FullScreen(imageUrl = viewModel.currentWallpaper?.imageUrl)
        }

    }
}

fun NavHostController.navigateToDetails(
    wallpaperId:String
) {
    navigate(Screens.Details.passWallpaperId(wallpaperId)) {
        launchSingleTop = true
    }
}