package com.example.jetwallpaper.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jetwallpaper.domain.models.Wallpaper

@Database(
    entities = [Wallpaper::class],
    version = 1,
    exportSchema = false
)
abstract class WallpaperDatabase:RoomDatabase() {
    abstract fun dao():WallpaperDao

    companion object{
        const val NAME = "wallpaper_db"
    }
}