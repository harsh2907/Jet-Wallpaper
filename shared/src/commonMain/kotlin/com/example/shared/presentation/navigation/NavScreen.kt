package com.example.shared.presentation.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jetwallpaper.ui.presentation.navigation.Screens
import com.example.shared.presentation.detailsScreen.DetailsScreen
import com.example.shared.presentation.detailsScreen.FullScreen
import com.example.shared.presentation.home.HomeScreen
import com.example.shared.presentation.home.SharedViewModel
import com.example.shared.presentation.util.UiEvent
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun JetWallpaperSharedNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
) {

    NavHost(
        navController = navController,
        startDestination = Screens.New.route,
        modifier = modifier
    ) {

        composable(route = Screens.New.route) {

                val wallpaperState by sharedViewModel.popularWallpapers.collectAsState()
                val uiEvent by sharedViewModel.uiEvent.collectAsState(initial = UiEvent.Idle)


                val snackbarHostState = remember { SnackbarHostState() }


                LaunchedEffect(uiEvent) {
                    if (uiEvent is UiEvent.ShowSnackBar) {
                        val action = snackbarHostState.showSnackbar(
                            message = (uiEvent as UiEvent.ShowSnackBar).message,
                            actionLabel = (uiEvent as UiEvent.ShowSnackBar).action,
                            duration = SnackbarDuration.Long
                        )
                        if (action == SnackbarResult.ActionPerformed) {
                            sharedViewModel.getPopularWallpapers()
                        }
                    }
                }

                HomeScreen(
                    wallpaperState = wallpaperState,
                    onEvent = sharedViewModel::sendUiEvent,
                    navigateToDetails = { wallpaper ->
                        sharedViewModel.updateCurrentWallpaper(wallpaper)
                        navController.navigateToDetails()
                    }
                )

        }

        composable(route = Screens.Search.route) {
        }

        composable(route = Screens.Favourite.route) {
        }

        composable(route = Screens.Details.route) {
            val uiEvent by sharedViewModel.uiEvent.collectAsState(initial = UiEvent.Idle)

            sharedViewModel.currentWallpaper?.let{wallpaper->
                DetailsScreen(
                    uiEvent = uiEvent,
                    wallpaper = wallpaper,
                    saveWallpaper = { },
                    navigateToFullScreen = { _ ->
                        navController.navigate(Screens.FullScreen.route)
                    },
                    onEvent = sharedViewModel::sendUiEvent,
                    onNavigateUp = navController::navigateUp
                )
            }
        }

        composable(route = Screens.FullScreen.route) {
            FullScreen(imageUrl = sharedViewModel.currentWallpaper?.imageUrl)
        }

    }
}

fun NavHostController.navigateToDetails() {

    navigate(Screens.Details.route) {
        launchSingleTop = true
    }
}

