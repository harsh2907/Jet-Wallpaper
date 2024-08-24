package com.example.shared.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.example.shared.domain.models.WallpaperItem


@Composable
fun WallpaperItem(
    wallpaper: WallpaperItem,
    onClick: (WallpaperItem) -> Unit
) {
   LoadImage(url = wallpaper.thumbnail, modifier = Modifier.clickable { onClick(wallpaper) })
}


@Composable
fun LoadImage(
    url: String,
    modifier: Modifier = Modifier
) {

    var showShimmer by remember { mutableStateOf(false) }

    AsyncImage(
        model = url,
        contentDescription = "image",
        contentScale = ContentScale.Crop,
        onState = { state ->
            showShimmer = when (state) {
                is AsyncImagePainter.State.Success -> false
                else -> true
            }
        },
        modifier = Modifier
            .height(300.dp)
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp))
            .then(modifier)
    )

}