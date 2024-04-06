package com.example.jetwallpaper.domain.repository

import androidx.paging.PagingData
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.domain.utils.RequestState
import kotlinx.coroutines.flow.Flow

interface WallpaperApiRepository {

     fun getPopularWallpapers(
          sortingParams:String
     ):Flow<PagingData<Wallpaper>>

     fun getNewWallpapers(
          sortingParams:String
     ) : Flow<PagingData<Wallpaper>>

     fun getSearchedWallpapers(
          search:String,
          sortingParams:String
     ) : Flow<PagingData<Wallpaper>>

     fun getWallpaperById(id:String):Flow<RequestState<Wallpaper>>

}