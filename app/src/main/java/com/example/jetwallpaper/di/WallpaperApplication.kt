package com.example.jetwallpaper.di

import android.app.Application
import com.example.shared.di.initKoin
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext

@HiltAndroidApp
class WallpaperApplication:Application(){
    override fun onCreate() {
        super.onCreate()
        initKoin{
            androidContext(this@WallpaperApplication)
        }
    }
}