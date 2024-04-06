package com.example.jetwallpaper

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.example.jetwallpaper.ui.presentation.screens.main.ErrorComponent
import com.example.jetwallpaper.ui.presentation.screens.main.JetWallpaperNavScreen
import com.example.jetwallpaper.ui.theme.JetWallpaperTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            JetWallpaperTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    var hasPermission by remember { mutableStateOf(true) }

                    val requestPermission = rememberMultiplePermissionsState(
                        permissions = when {
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                                listOf(
                                    android.Manifest.permission.READ_MEDIA_IMAGES
                                )
                            }

                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                                listOf(
                                    android.Manifest.permission.ACCESS_NETWORK_STATE
                                )
                            }

                            else -> {
                                listOf(
                                    android.Manifest.permission.ACCESS_NETWORK_STATE,
                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                                )
                            }
                        }
                    )


                    LifecycleResumeEffect(key1 = Unit) {
                        requestPermission.launchMultiplePermissionRequest()

                        onPauseOrDispose {  }
                    }

                    requestPermission.permissions.map { ps ->
                        hasPermission = ps.status.isGranted
                    }

                    AnimatedContent(
                        targetState = hasPermission,
                        transitionSpec = {
                            (slideInVertically() + fadeIn())
                                .togetherWith(slideOutVertically() + fadeOut())
                        },
                        label = ""
                    ) { targetState ->
                        if (targetState) {
                            JetWallpaperNavScreen()
                        } else {
                            ErrorComponent(
                                message = "Storage permission is required for downloading wallpapers." +
                                        " Please enable it from settings of the app"
                            )
                        }
                    }
                }
            }
        }
    }

}

