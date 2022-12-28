package com.example.jetwallpaper.ui.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.jetwallpaper.data.network.WallpaperResponse
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.domain.repository.WallpaperRepository
import com.example.jetwallpaper.domain.utils.Constants
import com.example.jetwallpaper.domain.utils.Response
import com.example.jetwallpaper.ui.presentation.viewmodel.MainViewModel.Companion.PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException

class PopularWallpaperPagingSource(
    private val api:WallpaperResponse
): PagingSource<Int, Wallpaper>()
{
    override fun getRefreshKey(state: PagingState<Int, Wallpaper>): Int? {
        return state.anchorPosition?.let {
            val page = state.closestPageToPosition(it)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Wallpaper> {
            val position = params.key ?: 1
            return try {

            val response = api.getWallpapers(
                queryParam = Constants.POPULAR,
                sorting = Constants.sortingPopular,
                page = params.loadSize
            )

                val nextKey =  position + (params.loadSize / PAGE_SIZE)
                val data = response.data.map { it.toWallpaper() }
                LoadResult.Page(
                    data = data,
                    prevKey = if(position == 1) null else position - 1,
                    nextKey = if(response.data.isEmpty()) null else nextKey
                )

        }catch (e: HttpException){
           LoadResult.Error(e)
        }
        catch (e: IOException){
            LoadResult.Error(e)
        }
    }

}


class NewWallpaperPagingSource(
    private val api:WallpaperResponse
): PagingSource<Int, Wallpaper>()
{
    override fun getRefreshKey(state: PagingState<Int, Wallpaper>): Int? {
        return state.anchorPosition?.let {
            val page = state.closestPageToPosition(it)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Wallpaper> {
        val position = params.key ?: 1

        return try {

            val response = api.getWallpapers(
                queryParam = Constants.NEW,
                sorting = Constants.sortingPopular,
                page = position
            )

            val nextKey =  position + (params.loadSize / PAGE_SIZE)
            val data = response.data.map { it.toWallpaper() }
            LoadResult.Page(
                data = data,
                prevKey = if(position == 1) null else position - 1,
                nextKey = if(response.data.isEmpty()) null else nextKey
            )

        }catch (e: HttpException){
            LoadResult.Error(e)
        }
        catch (e: IOException){
            LoadResult.Error(e)
        }
    }

}

class SearchPagingSource(
    private val api: WallpaperResponse,
    private val searchQuery:String,
): PagingSource<Int, Wallpaper>()
{
    override fun getRefreshKey(state: PagingState<Int, Wallpaper>): Int? {
        return state.anchorPosition?.let {
            val page = state.closestPageToPosition(it)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Wallpaper> {
        val position = params.key ?: 1
        return try {

            val response = api.getWallpapers(
                queryParam = searchQuery,
                sorting = Constants.sortingPopular,
                page = position
            )

            val nextKey =  position + (params.loadSize / PAGE_SIZE)
            val data = response.data.map { it.toWallpaper() }


            LoadResult.Page(
                data = data,
                prevKey = if(position == 1) null else position - 1,
                nextKey = if(response.data.isEmpty())  null else nextKey
            )

        }catch (e: HttpException){
            LoadResult.Error(e)
        }
        catch (e: IOException){
            LoadResult.Error(e)
        }
    }

}