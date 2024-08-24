package com.example.shared.presentation.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shared.domain.models.WallpaperItem
import com.example.shared.presentation.WallpaperState
import com.example.shared.presentation.components.CustomLoading
import com.example.shared.presentation.components.LazyWallpaperGrid
import com.example.shared.presentation.util.UiEvent

@Composable
fun HomeScreen(
    wallpaperState: WallpaperState,
    navigateToDetails: (WallpaperItem) -> Unit,
    onEvent: (UiEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->

        if (wallpaperState.isLoading){
            CustomLoading()
        } else{
            LazyWallpaperGrid(
                modifier = Modifier.fillMaxSize().padding(padding),
                wallpaperState = wallpaperState,
                onEvent = onEvent,
                onClick = navigateToDetails
            )
        }


    }
}