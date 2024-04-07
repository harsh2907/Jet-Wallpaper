package com.example.jetwallpaper.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.jetwallpaper.data.network.WallpaperResponse
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.data.utils.Constants
import retrofit2.HttpException
import java.io.IOException


class SearchPagingSource(
    private val api: WallpaperResponse,
    private val searchQuery:String,
    private val sortingParams:String
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
                sorting = sortingParams,
                page = position
            )

            val nextKey =  position + (params.loadSize / Constants.PAGE_SIZE)
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