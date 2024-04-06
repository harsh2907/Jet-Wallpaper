package com.example.jetwallpaper.ui.presentation.screens.wallpaperDetailsScreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun FullScreen(imageUrl: String?) {

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }

    val animatedZoomScale by animateFloatAsState(targetValue = scale, label = "zoom")


    AsyncImage(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build()
        ,
        contentDescription = "wallpaper",
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(
                scaleX = animatedZoomScale,
                scaleY = animatedZoomScale,
                translationX = offset.x,
                translationY = offset.y
            )
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    // Ensure the scale is within a reasonable range to prevent the image from becoming too small or too large
                    scale = (scale * zoom).coerceIn(1f, 3f)

                    // Update the offset to implement panning when zoomed.
                    offset = if (scale == 1f) Offset(0f, 0f) else offset + pan
                }
            },
        contentScale = ContentScale.Fit
    )

}