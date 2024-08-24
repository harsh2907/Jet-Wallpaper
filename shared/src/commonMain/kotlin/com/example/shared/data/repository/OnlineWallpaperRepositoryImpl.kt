package com.example.shared.data.repository

import com.example.shared.data.utils.Constants
import com.example.jetwallpaper.domain.utils.Response
import com.example.shared.data.network.WallpaperClient
import com.example.shared.domain.models.WallpaperItem
import com.example.shared.domain.repository.OnlineWallpaperRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OnlineWallpaperRepositoryImpl(
    private val api: WallpaperClient,
) : OnlineWallpaperRepository {

    override fun getPopularWallpapers(): Flow<Response<List<WallpaperItem>>> = flow {
        emit(Response.Loading())

        val response = api.getWallpapers(
            query = Constants.POPULAR,
            page = 1,
            sorting = Constants.sortingPopular
        )

        response
            .onSuccess {searchResult->
                val wallpapers = searchResult.data.map { it.toWallpaper() }
                emit(Response.Success(wallpapers))
            }.onFailure {
            emit(Response.Error(it.message ?:  "An unknown error occurred"))
        }
    }

    override fun getNewWallpapers(): Flow<Response<List<WallpaperItem>>> = flow {
        emit(Response.Loading())

        val response = api.getWallpapers(
            query = Constants.NEW,
            page = 1,
            sorting = Constants.sortingPopular
        )

        response
            .onSuccess {searchResult->
                val wallpapers = searchResult.data.map { it.toWallpaper() }
                emit(Response.Success(wallpapers))
            }.onFailure {
            emit(Response.Error(it.message ?:  "An unknown error occurred"))
        }
    }

    override fun getSearchedWallpapers(searchQuery: String): Flow<Response<List<WallpaperItem>>> = flow {
        emit(Response.Loading())

        val response = api.getWallpapers(
            query = searchQuery,
            page = 1,
            sorting = Constants.sortingPopular
        )

        response
            .onSuccess {searchResult->
                val wallpapers = searchResult.data.map { it.toWallpaper() }
                emit(Response.Success(wallpapers))
            }.onFailure {
            emit(Response.Error(it.message ?:  "An unknown error occurred"))
        }
    }

}