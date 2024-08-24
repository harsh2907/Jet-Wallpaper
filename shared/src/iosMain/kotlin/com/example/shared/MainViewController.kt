package com.example.shared

import androidx.compose.ui.window.ComposeUIViewController
import com.example.shared.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) { App() }