package com.example.jetwallpaper.ui.presentation.screens.details_screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import coil.compose.rememberImagePainter
import com.example.jetwallpaper.ui.presentation.viewmodel.MainViewModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun FullScreen(viewModel: MainViewModel) {

    val context = LocalContext.current
    val currentWallpaper = viewModel.currentWallpaper

    var angle by remember { mutableStateOf(0f) }
    var zoom by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    if (currentWallpaper == null)
        Toast.makeText(context, "Can't load image due to unknown error", Toast.LENGTH_SHORT).show()
    else
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Image(
                painter = rememberImagePainter(data = currentWallpaper.imageUrl),
                contentDescription = "wallpaper",
                modifier = Modifier
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .graphicsLayer(
                        scaleX = zoom,
                        scaleY = zoom,
                      //  rotationZ = angle
                    )
                    .pointerInput(Unit) {
                        detectTransformGestures(
                            onGesture = { _, pan, gestureZoom, gestureRotate ->
                                angle += gestureRotate
                                zoom *= gestureZoom
                                val x = pan.x * zoom
                                val y = pan.y * zoom
                                val angleRad = angle * PI / 180.0
                                offsetX += (x * cos(angleRad) - y * sin(angleRad)).toFloat()
                                offsetY += (x * sin(angleRad) + y * cos(angleRad)).toFloat()
                            }
                        )
                    }
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
        }
}