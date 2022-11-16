package com.example.jetwallpaper.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jetwallpaper.domain.models.Wallpaper
import kotlinx.coroutines.flow.Flow

@Dao
interface WallpaperDao {

    @Query("SELECT * FROM wallpaper ORDER BY createdAt DESC")
    fun getWallpapers():Flow<List<Wallpaper>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpaper(wallpaper: Wallpaper)

    @Delete
    suspend fun deleteWallpaper(wallpaper: Wallpaper)

}