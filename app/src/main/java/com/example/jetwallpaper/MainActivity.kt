package com.example.jetwallpaper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.shared.App
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
/*            JetWallpaperTheme {
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
            }*/
        }
    }

}

