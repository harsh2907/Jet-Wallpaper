package com.example.jetwallpaper.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.jetwallpaper.data.network.WallpaperResponse
import com.example.jetwallpaper.data.pagination.NewWallpaperPagingSource
import com.example.jetwallpaper.data.pagination.PopularWallpaperPagingSource
import com.example.jetwallpaper.data.pagination.SearchPagingSource
import com.example.jetwallpaper.data.utils.Constants
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.domain.repository.WallpaperApiRepository
import com.example.jetwallpaper.domain.utils.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WallpaperApiRepositoryImpl(
    private val api: WallpaperResponse
) : WallpaperApiRepository {

    override fun getPopularWallpapers(
        sortingParams:String
    ): Flow<PagingData<Wallpaper>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false
            )
        ) { PopularWallpaperPagingSource(api,sortingParams) }.flow
    }

    override fun getNewWallpapers(
        sortingParams:String
    ): Flow<PagingData<Wallpaper>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false
            )
        ) { NewWallpaperPagingSource(api,sortingParams) }.flow

    }

    override fun getSearchedWallpapers(
        search: String,
        sortingParams:String
    ): Flow<PagingData<Wallpaper>> {

        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false
            )
        ) { SearchPagingSource(api, search,sortingParams) }.flow
    }

    override fun getWallpaperById(id: String): Flow<RequestState<Wallpaper>> = flow {
        emit(RequestState.Loading)
        try {
           val wallpaper =  api.getWallpaperDetails(id= id)?.data?.toWallpaper()

            if(wallpaper!=null){
                emit(RequestState.Success(wallpaper))
            }else{
                emit(RequestState.Error("Can't load wallpaper, please try again later."))
            }

        }catch (e: HttpException){
            emit(RequestState.Error("Please check your internet connection and try again"))
        }
        catch (e: IOException){
            emit(RequestState.Error(e.message ?: "Oops,an unknown error occurred"))
        }
    }

}