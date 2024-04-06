package com.example.jetwallpaper.ui.presentation.screens.wallpaperDetailsScreen

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.ui.presentation.screens.main.UiEvent
import com.example.jetwallpaper.ui.presentation.utils.WallpaperUtils
import com.example.jetwallpaper.ui.presentation.utils.bounceClick
import com.example.jetwallpaper.ui.presentation.utils.openUrl
import com.example.jetwallpaper.ui.presentation.utils.parseSize
import com.example.jetwallpaper.ui.presentation.utils.shareUrl
import com.example.jetwallpaper.ui.theme.UiColors
import com.example.jetwallpaper.ui.util.CustomLoading
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    uiEvent: UiEvent,
    wallpaperDetailsState: WallpaperDetailsState,
    saveWallpaper: (Wallpaper) -> Unit,
    navigateToFullScreen: (String) -> Unit,
    onEvent: (UiEvent) -> Unit,
    reloadWallpaper: () -> Unit
) {

    val context = LocalContext.current as Activity
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val snackbarHostState = remember { SnackbarHostState() }
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val iconsRowHeight = remember { (screenHeight * 0.08).dp }
    var showShimmer by remember { mutableStateOf(true) }

    val sheetSize by animateDpAsState(
        targetValue = if (showShimmer) 0.dp else BottomSheetDefaults.SheetPeekHeight + iconsRowHeight,
        label = "sheet size"
    )

    LaunchedEffect(key1 = uiEvent) {
        when (uiEvent) {
            is UiEvent.ShowSnackBar -> {
                val action = snackbarHostState.showSnackbar(
                    message = uiEvent.message,
                    actionLabel = uiEvent.action,
                    duration = SnackbarDuration.Long
                )
                if (action == SnackbarResult.ActionPerformed) {
                    reloadWallpaper()
                }
            }

            is UiEvent.Idle -> Unit
        }
    }

    LaunchedEffect(wallpaperDetailsState) {
        when {

            wallpaperDetailsState.isLoading -> showShimmer = true

            wallpaperDetailsState.error.isNotEmpty() -> {
                onEvent(
                    UiEvent.ShowSnackBar(wallpaperDetailsState.error, "Retry")
                )
                showShimmer = false
            }

        }
    }




    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContainerColor = UiColors.BottomNavColor,
        sheetPeekHeight = sheetSize,
        sheetContent = {
            wallpaperDetailsState.wallpaper?.let { wallpaper ->
                BottomSheetIconsRow(
                    rowHeight = iconsRowHeight,
                    wallpaper = wallpaper,
                    onDownload = { imageUrl, imageId ->
                        scope.launch {
                            WallpaperUtils.saveBitmapToStorage(
                                context = context,
                                imageUrl = imageUrl,
                                imageId = imageId,
                                onSuccess = {
                                    Toast.makeText(
                                        context,
                                        "Wallpaper saved successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onFailed = { exception ->
                                    Log.e(
                                        "Save Wallpaper",
                                        exception.message,
                                        exception
                                    )

                                    Toast.makeText(
                                        context,
                                        "Failed to save wallpaper. Please try again",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    },
                    onSetFavourite = saveWallpaper,
                    onFullView = navigateToFullScreen,
                    onOpenBrowser = {
                        context.openUrl(it)
                    },
                    onShare = {
                        context.shareUrl(it)
                    }
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )

                Text(
                    text = "Views : ${wallpaper.views}",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(12.dp)
                )
                Text(
                    text = "Category : ${wallpaper.category}",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(12.dp)
                )
                Text(
                    text = "Resolution : ${wallpaper.resolution}",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(12.dp)
                )
                Text(
                    text = "File Size : ${parseSize(wallpaper.fileSize)}",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(12.dp)
                )
            }
        },
        sheetShape = ShapeDefaults.Medium,
        containerColor = if (isSystemInDarkTheme()) Color.Black else MaterialTheme.colorScheme.background
    ) {
        AnimatedVisibility(
            visible = showShimmer,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CustomLoading()
            }
        }

        wallpaperDetailsState.wallpaper?.let { wallpaper ->

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(wallpaper.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = wallpaper.id,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Fit,
                onState = { state ->
                    showShimmer = when (state) {
                        is AsyncImagePainter.State.Success -> false
                        is AsyncImagePainter.State.Error -> {
                            onEvent(
                                UiEvent.ShowSnackBar(
                                    message = "An unknown error occurred.Please try again later",
                                    action = "Retry"
                                )
                            )
                            false
                        }

                        else -> true
                    }
                }
            )

        }
    }
}


@Composable
fun BottomSheetIconsRow(
    rowHeight: Dp,
    wallpaper: Wallpaper,
    onDownload: (String, String) -> Unit,
    onSetFavourite: (Wallpaper) -> Unit,
    onOpenBrowser: (String) -> Unit,
    onFullView: (String) -> Unit,
    onShare: (String) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(rowHeight),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Download,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .padding(8.dp)
                .bounceClick { onDownload(wallpaper.imageUrl, wallpaper.id) }
        )
        Icon(
            imageVector = Icons.Outlined.Favorite,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .padding(8.dp)
                .bounceClick {
                    onSetFavourite(wallpaper)
                }
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .padding(8.dp)
                .bounceClick {
                    onOpenBrowser(wallpaper.url)
                }
        )
        Icon(
            imageVector = Icons.Default.Landscape,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .padding(8.dp)
                .bounceClick {
                    onFullView(wallpaper.imageUrl)
                }
        )
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .padding(10.dp)
                .bounceClick {
                    onShare(wallpaper.url)
                }
        )
    }
}

