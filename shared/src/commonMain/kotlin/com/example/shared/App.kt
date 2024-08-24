package com.example.shared

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import coil3.SingletonImageLoader.setSafe
import com.example.shared.presentation.navigation.JetWallpaperSharedNavigation
import com.example.shared.presentation.util.getAsyncImageLoader
import org.koin.compose.KoinContext

@Composable
fun App() {
    MaterialTheme {
        setSafe { context -> getAsyncImageLoader(context) }
        val navController = rememberNavController()

        KoinContext {
            JetWallpaperSharedNavigation(
                navController = navController
            )
        }

    }
}