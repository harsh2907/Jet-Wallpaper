package com.example.shared.domain.repository

import com.example.jetwallpaper.domain.utils.Response
import com.example.shared.domain.models.WallpaperItem
import kotlinx.coroutines.flow.Flow


interface OnlineWallpaperRepository {

     fun getPopularWallpapers(): Flow<Response<List<WallpaperItem>>>

     fun getNewWallpapers(): Flow<Response<List<WallpaperItem>>>

     fun getSearchedWallpapers(searchQuery: String): Flow<Response<List<WallpaperItem>>>
}