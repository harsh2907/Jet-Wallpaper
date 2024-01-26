package com.example.jetwallpaper.data.network

import com.example.jetwallpaper.domain.models.details.WallDetailsDTO
import com.example.jetwallpaper.domain.models.search_result.SearchResultDTO
import com.example.jetwallpaper.domain.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WallpaperResponse {

    @GET("/api/v1/search/")
    suspend fun getWallpapers(
        @Query("q") queryParam: String,
        @Query("sorting") sorting: String,
        @Query("page") page: Int,
        @Query("purity") purity: Int = 100,
        @Query("apikey") apiKey:String = Constants.API_KEY
    ):SearchResultDTO

    @GET("/api/v1/w/{id}")
    suspend fun getWallpaperDetails(
        @Path("id") id: String,
        @Query("apikey") apiKey:String = Constants.API_KEY
    ): WallDetailsDTO

}