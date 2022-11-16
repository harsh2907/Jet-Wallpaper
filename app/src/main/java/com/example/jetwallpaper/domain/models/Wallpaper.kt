package com.example.jetwallpaper.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Wallpaper(
    @PrimaryKey val id: String,
    val createdAt: String,
    val fileSize: Int,
    val imageUrl: String,
    val resolution: String,
    val views: Int,
    val category: String,
    val url: String,
    val thumbnail:String
)
