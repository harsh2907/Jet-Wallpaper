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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WallpaperRepositoryImpl(
    private val api:WallpaperResponse,
    private val dao: WallpaperDao
):WallpaperRepository {

    override  fun getPopularWallpapers(pageSize:Int):Flow<PagingData<Wallpaper>>{
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                prefetchDistance = 6,
                enablePlaceholders = false,
                initialLoadSize = 12
            ),
            pagingSourceFactory = {
                PopularWallpaperPagingSource(api)
            }
        ).flow
    }

    override fun getNewWallpapers(pageSize : Int):Flow<PagingData<Wallpaper>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                prefetchDistance = 6,
                enablePlaceholders = false,
                initialLoadSize = pageSize
            ),
            pagingSourceFactory = {
                NewWallpaperPagingSource(api)
            }
        ).flow

    }

    override  fun getSearchedWallpapers(search:String,page: Int): Flow<Response<SearchResultDTO>> = flow {
        try {
            emit(Response.Loading())
            val response = api.getWallpapers(
                queryParam = search,
                sorting = Constants.sortingNew,
                page = page
            )

            emit(Response.Success(response))

        }catch (e: HttpException){
            emit(Response.Error(error = "Oops, something went wrong"))
        }
        catch (e: IOException){
            emit(Response.Error(error = "Couldn't reach server check your internet connection"))
        }

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