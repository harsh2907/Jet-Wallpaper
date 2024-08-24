package com.example.shared.data.network

import com.example.shared.domain.models.details.WallpaperDetailsDTO
import com.example.shared.domain.models.search_result.SearchResultDTO
import com.example.shared.data.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class WallpaperClient(
    private val httpClient: HttpClient
) {

    suspend fun getWallpapers(
        query: String,
        sorting: String,
        page: Int,
        purity: Int = 100
    ): Result<SearchResultDTO> {
        val response = try {
            httpClient.get(
                urlString = "${Constants.BASE_URL}/api/v1/search"
            ) {
                parameter("q", query)
                parameter("sorting", sorting)
                parameter("page", page)
                parameter("purity", purity)
                parameter("apikey", Constants.API_KEY)
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }

        return when(response.status.value){
            in 200..299 ->{
                val wallpapers = response.body<SearchResultDTO>()
                Result.success(wallpapers)
            }
            else->{
                Result.failure(Exception("Oops, an unknown error occurred"))
            }
        }
    }

    suspend fun getWallpaperById(
        id:String
    ): Result<WallpaperDetailsDTO> {
        val response = try {
            httpClient.get(
                urlString = "${Constants.BASE_URL}/api/v1/w/${id}"
            ) {
                parameter("apikey", Constants.API_KEY)
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }

        return when(response.status.value){
            in 200..299 ->{
                val wallpapers = response.body<WallpaperDetailsDTO>()
                Result.success(wallpapers)
            }
            else->{
                Result.failure(Exception("Oops, an unknown error occurred"))
            }
        }
    }
}