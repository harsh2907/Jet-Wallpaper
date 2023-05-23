package com.example.jetwallpaper.ui.presentation.screens.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.jetwallpaper.ui.presentation.navigation.NavScreen
import com.example.jetwallpaper.ui.presentation.viewmodel.MainViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
    ) {
        NavScreen(navController = navController,viewModel)
    }
}