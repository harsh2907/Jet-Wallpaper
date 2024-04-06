package com.example.jetwallpaper.di

import android.content.Context
import androidx.room.Room
import com.example.jetwallpaper.data.local.WallpaperDatabase
import com.example.jetwallpaper.data.network.WallpaperResponse
import com.example.jetwallpaper.data.repository.WallpaperApiRepositoryImpl
import com.example.jetwallpaper.data.repository.WallpaperDatabaseRepositoryImpl
import com.example.jetwallpaper.domain.repository.WallpaperApiRepository
import com.example.jetwallpaper.data.utils.Constants
import com.example.jetwallpaper.domain.repository.WallpaperDatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): WallpaperResponse = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WallpaperResponse::class.java)

    @Provides
    @Singleton
    fun provideWallpaperDatabase(@ApplicationContext context: Context):WallpaperDatabase = Room.databaseBuilder(
        context,
        WallpaperDatabase::class.java,
        WallpaperDatabase.NAME
    ).build()

    @Singleton
    @Provides
    fun provideWallpaperApiRepository(api: WallpaperResponse ): WallpaperApiRepository =
        WallpaperApiRepositoryImpl(api)

    @Singleton
    @Provides
    fun provideWallpaperDatabaseRepository( db: WallpaperDatabase): WallpaperDatabaseRepository =
        WallpaperDatabaseRepositoryImpl(db.dao())

}