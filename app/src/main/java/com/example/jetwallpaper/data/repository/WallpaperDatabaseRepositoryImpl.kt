package com.example.jetwallpaper.data.repository

import com.example.jetwallpaper.data.local.WallpaperDao
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.domain.repository.WallpaperDatabaseRepository
import kotlinx.coroutines.flow.Flow

class WallpaperDatabaseRepositoryImpl(
    private val dao: WallpaperDao
) : WallpaperDatabaseRepository {

    override fun getFavouriteWallpapers(): Flow<List<Wallpaper>> {
        return dao.getWallpapers()
    }

    override suspend fun insertWallpaper(wallpaper: Wallpaper){
        return dao.insertWallpaper(wallpaper)
    }

    override suspend fun deleteWallpaper(wallpaper: Wallpaper) {
        return dao.deleteWallpaper(wallpaper)
    }
}