package com.example.jetwallpaper.ui.presentation.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.jetwallpaper.navigation.JetWallpaperNavigation


@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { WallpaperBottomNavigationBar(navController = navController) }
    ) { padding ->
        JetWallpaperNavigation(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        )
    }
}