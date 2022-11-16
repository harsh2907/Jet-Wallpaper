package com.example.jetwallpaper.domain.repository

import androidx.paging.PagingData
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.domain.models.search_result.SearchResultDTO
import com.example.jetwallpaper.domain.utils.Response
import kotlinx.coroutines.flow.Flow

interface WallpaperRepository {

     fun getPopularWallpapers(pageSize:Int):Flow<PagingData<Wallpaper>>

     fun getNewWallpapers(pageSize: Int) : Flow<PagingData<Wallpaper>>

     fun getSearchedWallpapers(search:String,page: Int) : Flow<Response<SearchResultDTO>>

     fun getFavouriteWallpapers():Flow<List<Wallpaper>>

     suspend fun insertWallpaper(wallpaper: Wallpaper)

     suspend fun deleteWallpaper(wallpaper: Wallpaper)
}