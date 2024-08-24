package com.example.shared.presentation.detailsScreen


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.example.shared.domain.models.WallpaperItem
import com.example.shared.presentation.components.bounceClick
import com.example.shared.presentation.util.UiEvent
import com.example.shared.presentation.util.Utils
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    uiEvent: UiEvent,
    wallpaper: WallpaperItem,
    saveWallpaper: (WallpaperItem) -> Unit,
    navigateToFullScreen: (imageUrl: String) -> Unit,
    onNavigateUp: () -> Unit,
    onEvent: (UiEvent) -> Unit
) {

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    )
    val snackbarHostState = scaffoldState.snackbarHostState

    LaunchedEffect(uiEvent) {
        if (uiEvent is UiEvent.ShowSnackBar) {
            scope.launch {
                val action = snackbarHostState.showSnackbar(
                    message = uiEvent.message,
                    actionLabel = uiEvent.action,
                    duration = SnackbarDuration.Long
                )
                if (action == SnackbarResult.ActionPerformed) {

                }
            }
        }
    }


    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Wallpaper")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateUp,
                        content = {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "back"
                            )
                        }
                    )
                }
            )
        },
        sheetContent = {
            BottomSheetIcons(
                wallpaper = wallpaper,
                onDownload = { imageUrl, imageId ->

                },
                onSetFavourite = saveWallpaper,
                onFullView = navigateToFullScreen,
                onOpenBrowser = {

                },
                onShare = {

                }
            )
            Spacer(
                modifier = Modifier
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
                text = "File Size : ${Utils.parseSize(wallpaper.fileSize)}",
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(12.dp)
            )
        },
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = if (isSystemInDarkTheme()) Color.Black else MaterialTheme.colorScheme.background
    ) {


        var showShimmer by remember { mutableStateOf(false) }

        AsyncImage(
            model = wallpaper.imageUrl,
            contentDescription = wallpaper.id,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
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
                        true
                    }

                    else -> true
                }
            }
        )
    }

}

@Composable
fun BottomSheetIcons(
    wallpaper: WallpaperItem,
    onDownload: (String, String) -> Unit,
    onSetFavourite: (WallpaperItem) -> Unit,
    onOpenBrowser: (String) -> Unit,
    onFullView: (imageUrl: String) -> Unit,
    onShare: (String) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(12.dp),
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
            imageVector = Icons.Default.OpenInNew,
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

