package com.example.jetwallpaper.ui.presentation.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetwallpaper.ui.presentation.navigation.JetWallpaperNavScreenContent


@Composable
fun JetWallpaperNavScreen() {
    val navController = rememberNavController()
    val navBackStack by navController.currentBackStackEntryAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            val showBottom =
                bottomNavItems.map { it.route }.contains(navBackStack?.destination?.route)
            AnimatedVisibility(visible = showBottom) {
                BottomNavigationBar(navController = navController)
            }
        }) { padding ->
        JetWallpaperNavScreenContent(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        )
    }
}