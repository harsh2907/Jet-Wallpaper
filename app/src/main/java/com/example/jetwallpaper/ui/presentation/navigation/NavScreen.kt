package com.example.jetwallpaper.ui.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.ui.presentation.screens.details_screen.DetailsScreen
import com.example.jetwallpaper.ui.presentation.screens.details_screen.FullScreen
import com.example.jetwallpaper.ui.presentation.screens.favourite_screen.FavouriteScreen
import com.example.jetwallpaper.ui.presentation.screens.new_screen.NewScreen
import com.example.jetwallpaper.ui.presentation.screens.popular_screen.PopularScreen
import com.example.jetwallpaper.ui.presentation.viewmodel.MainViewModel

@Composable
fun NavScreen(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = Screens.New.route) {
        composable(route = Screens.Popular.route) {
            PopularScreen(viewModel, navController)
        }
        composable(route = Screens.New.route) {
            NewScreen(viewModel, navController)
        }
        composable(route = Screens.Favourite.route) {
            FavouriteScreen(navController,viewModel)
        }
        composable(route = Screens.Details.route) {
            DetailsScreen(viewModel, navController)
        }
        composable(route = Screens.FullScreen.route) {
            FullScreen(viewModel)
        }

    }
}

fun NavHostController.navigateToDetails(viewModel: MainViewModel, wallpaper: Wallpaper) {
    viewModel.currentWallpaper = wallpaper
    Log.d("NavigateToDetails", viewModel.currentWallpaper.toString())
    navigate(Screens.Details.route) {
        launchSingleTop = true
    }
}