package com.example.shared.di

import android.app.Application
import org.koin.android.ext.koin.androidContext

class JetWallpaperApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin{
            androidContext(this@JetWallpaperApplication)
        }
    }
}