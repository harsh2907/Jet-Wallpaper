package com.example.jetwallpaper.domain.repository

import com.example.jetwallpaper.domain.models.Wallpaper
import kotlinx.coroutines.flow.Flow

interface WallpaperDatabaseRepository {

    fun getFavouriteWallpapers(): Flow<List<Wallpaper>>

    suspend fun insertWallpaper(wallpaper: Wallpaper)

    suspend fun deleteWallpaper(wallpaper: Wallpaper)

}