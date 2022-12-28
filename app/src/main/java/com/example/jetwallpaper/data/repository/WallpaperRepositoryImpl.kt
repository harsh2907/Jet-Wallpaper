package com.example.jetwallpaper.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.jetwallpaper.data.local.WallpaperDao
import com.example.jetwallpaper.data.network.WallpaperResponse
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.domain.models.search_result.SearchResultDTO
import com.example.jetwallpaper.domain.repository.WallpaperRepository
import com.example.jetwallpaper.domain.utils.Constants
import com.example.jetwallpaper.domain.utils.Response
import com.example.jetwallpaper.ui.pagination.NewWallpaperPagingSource
import com.example.jetwallpaper.ui.pagination.PopularWallpaperPagingSource
import com.example.jetwallpaper.ui.pagination.SearchPagingSource
import com.example.jetwallpaper.ui.presentation.viewmodel.MainViewModel.Companion.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WallpaperRepositoryImpl(
    private val api:WallpaperResponse,
    private val dao: WallpaperDao
):WallpaperRepository {

    override  fun getPopularWallpapers():Flow<PagingData<Wallpaper>>{
        return Pager( config = PagingConfig( pageSize = PAGE_SIZE , enablePlaceholders = false )){ PopularWallpaperPagingSource(api) }.flow
    }

    override fun getNewWallpapers():Flow<PagingData<Wallpaper>> {
        return Pager( config = PagingConfig( pageSize = PAGE_SIZE , enablePlaceholders = false)){ NewWallpaperPagingSource(api) }.flow

    }

    override fun getSearchedWallpapers(search: String): Flow<PagingData<Wallpaper>> {

        return Pager( config = PagingConfig( pageSize = PAGE_SIZE , enablePlaceholders = false)){ SearchPagingSource(api,search) }.flow
    }


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