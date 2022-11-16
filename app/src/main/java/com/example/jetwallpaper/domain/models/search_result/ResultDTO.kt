package com.example.jetwallpaper.domain.models.search_result

import com.example.jetwallpaper.domain.models.Wallpaper

data class ResultDTO(
    val category: String,
    val colors: List<String>,
    val created_at: String,
    val dimension_x: Int,
    val dimension_y: Int,
    val favorites: Int,
    val file_size: Int,
    val file_type: String,
    val id: String,
    val path: String,
    val purity: String,
    val ratio: String,
    val resolution: String,
    val short_url: String,
    val source: String,
    val thumbs: Thumbs,
    val url: String,
    val views: Int
){
    fun toWallpaper():Wallpaper =  Wallpaper(
            id = id,
            createdAt = created_at,
            fileSize = file_size,
            resolution = resolution,
            category = category,
            imageUrl = path,
            views = views,
            url = url,
            thumbnail = thumbs.small
    )
}

