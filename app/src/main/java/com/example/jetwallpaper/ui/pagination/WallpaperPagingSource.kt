package com.example.jetwallpaper.ui.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.jetwallpaper.data.network.WallpaperResponse
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.domain.repository.WallpaperRepository
import com.example.jetwallpaper.domain.utils.Constants
import com.example.jetwallpaper.domain.utils.Response
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

                val nextKey = if(position >= response.meta.last_page) null else position + 1
                val data = response.data.map { it.toWallpaper() }
                LoadResult.Page(
                    data = data,
                    prevKey = if(position == 1) null else position - 1,
                    nextKey = nextKey
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
                sorting = Constants.sortingNew,
                page = position
            )

            val nextKey = if(position >= response.meta.last_page) null else position + 1
            val data = response.data.map { it.toWallpaper() }
            LoadResult.Page(
                data = data,
                prevKey = if(position == 1) null else position - 1,
                nextKey = nextKey
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
    private val repository:WallpaperRepository,
    private val searchQuery:String
): PagingSource<Int, Wallpaper>()
{
    override fun getRefreshKey(state: PagingState<Int, Wallpaper>): Int? {
        return state.anchorPosition?.let {
            val page = state.closestPageToPosition(it)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Wallpaper> {
        return try {
            val nextPage = params.key ?: 1
            val apiResponse:ArrayList<Wallpaper> = ArrayList()
            var page = 0
            var last = 0

            repository.getSearchedWallpapers(searchQuery,nextPage).collectLatest{res->
                if(res is Response.Success) {
                    val wallpaper = res.data.data.map { it.toWallpaper() }
                    apiResponse.addAll(wallpaper)
                    page = res.data.meta.current_page
                    last = res.data.meta.last_page
                }
            }

            LoadResult.Page(
                data = apiResponse,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if(page == last) null else page+1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}