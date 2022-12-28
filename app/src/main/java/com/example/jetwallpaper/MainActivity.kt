package com.example.jetwallpaper

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.jetwallpaper.ui.presentation.navigation.NavScreen
import com.example.jetwallpaper.ui.presentation.screens.main.ErrorComponent
import com.example.jetwallpaper.ui.presentation.screens.main.MainScreen
import com.example.jetwallpaper.ui.presentation.viewmodel.MainViewModel
import com.example.jetwallpaper.ui.theme.JetWallpaperTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            JetWallpaperTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val viewModel: MainViewModel = viewModel()
                    val hasPermission = rememberSaveable { mutableStateOf(true) }
                    window.statusBarColor = MaterialTheme.colors.background.toArgb()

                    val requestPermission = rememberMultiplePermissionsState(
                        permissions = listOf(
                            android.Manifest.permission.ACCESS_NETWORK_STATE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        )
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
                        transitionSpec = { slideInVertically() + fadeIn() with slideOutVertically() + fadeOut() }
                    ) { targetState ->
                        if (targetState) {
                            MainScreen(
                                navController = rememberNavController(),
                                viewModel = viewModel
                            )
                        } else
                            ErrorComponent(message = "Storage permission is required for downloading wallpapers. Please enable it from settings of the app")
                    }
                }
            }
        }
    }

}
