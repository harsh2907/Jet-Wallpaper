package com.example.jetwallpaper.ui.presentation.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jetwallpaper.ui.theme.Violet

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    loadingColor: Color = Violet
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center,){
        CircularProgressIndicator(
            color = loadingColor
        )
    }
}