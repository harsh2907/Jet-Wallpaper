package com.example.jetwallpaper.ui.presentation.utils

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer


@OptIn(ExperimentalCoilApi::class)
@Composable
fun LoadImage(
    url: String,
    modifier:Modifier = Modifier
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader(context)
    var showShimmer by remember{ mutableStateOf(false) }

    val request = ImageRequest.Builder(context)
        .data(url)
        .build()

    val painter = rememberImagePainter(
        request = request,
        imageLoader = imageLoader
    )

    val state = painter.state
    showShimmer = when(state){
        is ImagePainter.State.Success -> false
        else -> true
    }

    Image(
        painter = painter,
        contentDescription = "image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(300.dp)
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp))
            .placeholder(
                visible = showShimmer,
                color = Color.DarkGray,
                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.LightGray)
            )
            .then(modifier)
    )

}