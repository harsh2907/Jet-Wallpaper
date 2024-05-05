package com.example.jetwallpaper.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.jetwallpaper.ui.presentation.screens.exploreScreen.ExploreScreen
import com.example.jetwallpaper.ui.presentation.screens.favourite_screen.FavouriteScreen
import com.example.jetwallpaper.ui.presentation.screens.main.ExploreViewModel
import com.example.jetwallpaper.ui.presentation.screens.main.UiEvent
import com.example.jetwallpaper.ui.presentation.screens.newWallpapersScreen.NewWallpaperScreen
import com.example.jetwallpaper.ui.presentation.screens.newWallpapersScreen.NewWallpapersViewModel
import com.example.jetwallpaper.ui.presentation.screens.wallpaperDetailsScreen.DetailsScreen
import com.example.jetwallpaper.ui.presentation.screens.wallpaperDetailsScreen.FullScreen
import com.example.jetwallpaper.ui.presentation.screens.wallpaperDetailsScreen.WallpaperDetailsViewModel

@Composable
fun JetWallpaperNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    NavHost(
        navController = navController,
        startDestination = Screens.New.route,
        modifier = modifier,
        enterTransition = { slideInHorizontally { it } + fadeIn() },
        exitTransition = { slideOutHorizontally { -it } + fadeOut() },
        popEnterTransition = { slideInHorizontally { -it } + fadeIn() },
        popExitTransition = { slideOutHorizontally { it } + fadeOut() },
    ) {

        composable(route = Screens.Explore.route) {
            val exploreViewModel: ExploreViewModel = hiltViewModel()

            val wallpaperState = exploreViewModel.searchPager.collectAsLazyPagingItems()
            val uiEvent by exploreViewModel.uiEvent.collectAsStateWithLifecycle(initialValue = UiEvent.Idle)

            ExploreScreen(
                wallpaperState = wallpaperState,
                uiEvent = uiEvent,
                onEvent = exploreViewModel::sendUiEvent,
                navigateToDetails = navController::navigateToDetails,
                updateSearchList = { query ->
                    exploreViewModel.updateSearchList(query, exploreViewModel.accept)
                }
            )

        }

        composable(route = Screens.New.route) {

            val newWallpapersViewModel: NewWallpapersViewModel = hiltViewModel()

            val uiEvent by
            newWallpapersViewModel.uiEvent.collectAsStateWithLifecycle(initialValue = UiEvent.Idle)

            val selectedSortingParam by
            newWallpapersViewModel.getSortingParam().collectAsStateWithLifecycle()

            val wallpaperState = newWallpapersViewModel.wallpaperPager.collectAsLazyPagingItems()

            NewWallpaperScreen(
                uiEvent = uiEvent,
                wallpaperState = wallpaperState,
                selectedSortingParam = selectedSortingParam,
                onSortingParamChange = newWallpapersViewModel::setSortingParam,
                onEvent = newWallpapersViewModel::sendUiEvent,
                navigateToDetails = navController::navigateToDetails
            )

        }

        composable(route = Screens.Favourite.route) { backstack ->

            val exploreViewModel: ExploreViewModel = hiltViewModel()

            val uiEvent by exploreViewModel.uiEvent.collectAsStateWithLifecycle(initialValue = UiEvent.Idle)

            val savedWallpaperState by exploreViewModel.savedWallpapers
                .collectAsStateWithLifecycle()

            FavouriteScreen(
                uiEvent = uiEvent,
                savedWallpaperState = savedWallpaperState,
                onEvent = exploreViewModel::sendUiEvent,
                navigateToDetails = navController::navigateToDetails,
                onLongClick = exploreViewModel::deleteWallpaper
            )

        }

        wallpaperDetails(navController)

    }
}

fun NavGraphBuilder.wallpaperDetails(
    navController: NavHostController
) {
    navigation(
        route = Screens.WallpaperDetails.route,
        startDestination = Screens.WallpaperDetails.Details.route
    ) {
        composable(
            route = Screens.WallpaperDetails.Details.route,
            arguments = listOf(
                navArgument("wallpaperId") { type = NavType.StringType }
            )
        ) {
            val detailsViewModel: WallpaperDetailsViewModel = hiltViewModel()

            val uiEvent by detailsViewModel.uiEvent.collectAsStateWithLifecycle(initialValue = UiEvent.Idle)
            val wallpaperDetailsState by detailsViewModel.wallpaperDetailsState.collectAsStateWithLifecycle()

            DetailsScreen(
                uiEvent = uiEvent,
                wallpaperDetailsState = wallpaperDetailsState,
                toggleFavourite = detailsViewModel::toggleFavourite,
                onEvent = detailsViewModel::sendUiEvent,
                reloadWallpaper = detailsViewModel::reloadWallpaper,
                navigateToFullScreen = { _ ->
                    navController.navigate(Screens.WallpaperDetails.FullScreen.route)
                }
            )

        }


        composable(route = Screens.WallpaperDetails.FullScreen.route) { backstack ->

            val parentEntry = remember(backstack) {
                navController.getBackStackEntry(Screens.WallpaperDetails.Details.route)
            }

            val detailsViewModel: WallpaperDetailsViewModel = hiltViewModel(parentEntry)
            val wallpaperState by
            detailsViewModel.wallpaperDetailsState.collectAsStateWithLifecycle()

            FullScreen(imageUrl = wallpaperState.savableWallpaper.wallpaper?.imageUrl)
        }
    }


}

fun NavHostController.navigateToDetails(
    wallpaperId: String
) {
    navigate(Screens.WallpaperDetails.Details.passWallpaperId(wallpaperId)) {
        launchSingleTop = true
    }
}