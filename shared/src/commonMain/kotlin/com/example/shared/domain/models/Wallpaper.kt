package com.example.shared.domain.models


data class WallpaperItem(
    val id: String,
    val createdAt: String,
    val fileSize: Int,
    val imageUrl: String,
    val resolution: String,
    val views: Int,
    val category: String,
    val url: String,
    val thumbnail:String
)
