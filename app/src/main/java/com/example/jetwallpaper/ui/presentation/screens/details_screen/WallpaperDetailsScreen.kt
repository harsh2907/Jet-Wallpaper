package com.example.jetwallpaper.ui.presentation.screens.details_screen

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.ui.presentation.navigation.Screens
import com.example.jetwallpaper.ui.presentation.utils.*
import com.example.jetwallpaper.ui.presentation.viewmodel.MainViewModel
import com.example.jetwallpaper.ui.presentation.viewmodel.UiEvent
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterialApi::class, ExperimentalCoilApi::class)
@Composable
fun DetailsScreen(viewModel: MainViewModel, navController: NavHostController) {
    val currentWallpaper = viewModel.currentWallpaper
    val uiEvent = viewModel.uiEvent.collectAsState(initial = UiEvent.Idle).value

    currentWallpaper?.let { wallpaper ->

        val context = LocalContext.current as Activity
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = BottomSheetState(
                BottomSheetValue.Collapsed
            )
        )

        when (uiEvent) {
            is UiEvent.ShowSnackBar -> {
                LaunchedEffect(key1 = true) {
                    scope.launch {
                        val action = scaffoldState.snackbarHostState.showSnackbar(
                            message = uiEvent.message,
                            actionLabel = uiEvent.action,
                            duration = SnackbarDuration.Long
                        )
                        if (action == SnackbarResult.ActionPerformed) {

                        }
                    }
                }
            }
            is UiEvent.Idle -> Unit
        }


        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                BottomSheetIcons(
                    wallpaper = wallpaper,
                    onDownload = { url, id ->
                            scope.launch {
                                val isSaved = WallpaperUtils.saveImage(context, url, id)
                                withContext(Dispatchers.Main) {
                                    if (isSaved) Toast.makeText(
                                        context,
                                        "Wallpaper downloaded successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    },
                    onSetFavourite = {
                        viewModel.addWallpaper(it)
                        Toast.makeText(context, "Wallpaper added successfully", Toast.LENGTH_SHORT).show()
                    },
                    onFullView = {
                        navController.navigate(Screens.FullScreen.route)
                    },
                    onOpenBrowser = {
                        context.openUrl(it)
                    },
                    onShare = {
                        context.shareUrl(it)
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
                    text = "File Size : ${parseSize(wallpaper.fileSize)}",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(12.dp)
                )
            },
            sheetElevation = 20.dp,
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetBackgroundColor = if(isSystemInDarkTheme()) Color.Black else MaterialTheme.colors.background
        ) {

            val imagePainter = rememberImagePainter(data = wallpaper.imageUrl)
            var showShimmer by remember{ mutableStateOf(false) }

             showShimmer = when(imagePainter.state){
                 is ImagePainter.State.Success -> false
                 is ImagePainter.State.Error -> {
                     viewModel.sendUiEvent(
                         UiEvent.ShowSnackBar(
                             message = "An unknown error occurred.Please try again later",
                             action = "Retry"
                         )
                     )
                     true
                 }
                 else -> true
            }

            Image(
                painter = imagePainter,
                contentDescription = wallpaper.id,
                modifier = Modifier
                    .fillMaxSize()
                    .placeholder(
                        visible = showShimmer,
                        color = Color.DarkGray,
                        highlight = PlaceholderHighlight.shimmer(highlightColor = Color.LightGray)
                    ),
                contentScale = ContentScale.Crop
            )

        }
    }

}

@Composable
fun BottomSheetIcons(
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

