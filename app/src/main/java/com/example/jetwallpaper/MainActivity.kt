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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.jetwallpaper.ui.presentation.screens.main.ErrorComponent
import com.example.jetwallpaper.ui.presentation.screens.main.JetWallpaperNavScreen
import com.example.jetwallpaper.ui.theme.JetWallpaperTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
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
                    modifier = Modifier.fillMaxSize() ,
                    color = MaterialTheme.colorScheme.background
                ) {
                    val hasPermission = rememberSaveable { mutableStateOf(true) }

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


                    val lifecycleOwner = LocalLifecycleOwner.current
                    DisposableEffect(key1 = lifecycleOwner, effect = {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_RESUME) {
                                requestPermission.launchMultiplePermissionRequest()

                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)

                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    })

                    requestPermission.permissions.forEach { ps ->
                        if (ps.hasPermission) {
                            hasPermission.value = true
                        }
                        if (ps.shouldShowRationale) {
                            hasPermission.value = false
                        }
                        if (!ps.hasPermission && !ps.shouldShowRationale) {
                            hasPermission.value = false
                        }
                    }

                    AnimatedContent(
                        targetState = hasPermission.value,
                        transitionSpec = {
                            (slideInVertically() + fadeIn()).togetherWith(
                                slideOutVertically() + fadeOut()
                            )
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
