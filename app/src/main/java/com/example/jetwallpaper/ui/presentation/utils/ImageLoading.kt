package com.example.jetwallpaper.ui.presentation.utils

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer


@Composable
fun LoadImage(
    url: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showShimmer by remember { mutableStateOf(false) }
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val cardHeight=  (screenHeight*0.35).dp

    val request = ImageRequest
        .Builder(context)
        .data(url)
        .crossfade(true)
        .build()


    AsyncImage(
        model = request,
        contentDescription = "image",
        contentScale = ContentScale.Crop,
        onState = { state ->
            showShimmer = when (state) {
                is AsyncImagePainter.State.Success -> false
                else -> true
            }
        },
        modifier = Modifier
            .heightIn(max = cardHeight)
            .padding(12.dp)
            .clip(ShapeDefaults.Medium)
            .placeholder(
                visible = showShimmer,
                color = Color.DarkGray,
                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.LightGray)
            )
            .then(modifier)
    )

}