package com.example.jetwallpaper.ui.presentation.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.jetwallpaper.ui.presentation.navigation.NavScreen
import com.example.jetwallpaper.ui.presentation.navigation.Screens
import com.example.jetwallpaper.ui.presentation.viewmodel.MainViewModel

@Composable
fun MainScreen(
    navController:NavHostController,
    viewModel: MainViewModel
) {
    val navBackStack by navController.currentBackStackEntryAsState()
    Scaffold(modifier = Modifier.fillMaxSize(),
    bottomBar = {
        val showBottom = bottomNavItems.map { it.route }.contains(navBackStack?.destination?.route)
        AnimatedVisibility(visible = showBottom) {
            BottomNav(navController = navController)
        }
    }
    ) {paddingValues ->
        val p = paddingValues
        NavScreen(navController = navController,viewModel)
    }
}