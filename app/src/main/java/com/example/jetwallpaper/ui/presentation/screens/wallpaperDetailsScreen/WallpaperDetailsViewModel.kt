package com.example.jetwallpaper.ui.presentation.screens.wallpaperDetailsScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.domain.repository.WallpaperApiRepository
import com.example.jetwallpaper.domain.repository.WallpaperDatabaseRepository
import com.example.jetwallpaper.ui.presentation.screens.main.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallpaperDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val wallpaperApiRepository: WallpaperApiRepository,
    private val wallpaperDatabaseRepository: WallpaperDatabaseRepository,
) : ViewModel() {

    private val savedWallpaperIds = wallpaperDatabaseRepository.getFavouriteWallpapers()
        .map { it.map { wallpaper -> wallpaper.id } }

    private val _wallpaperState = MutableStateFlow(WallpaperDetailsState())
    val wallpaperDetailsState = _wallpaperState.asStateFlow()

    private fun getWallpaperById(id: String) {
        viewModelScope.launch {
            combine(
                wallpaperApiRepository.getWallpaperById(id),
                savedWallpaperIds
            ) { wallpaperState, savedWallpapers ->
                val wallpaper = wallpaperState.getSuccessDataOrNull()
                val savableWallpaper = SavableWallpaper(
                    wallpaper = wallpaper,
                    isFavourite = wallpaper?.id?.let { id -> savedWallpapers.contains(id) }
                        ?: false
                )

                WallpaperDetailsState(
                    savableWallpaper = savableWallpaper,
                    isLoading = wallpaperState.isLoading(),
                    error = wallpaperState.getErrorMessageOrNull() ?: ""
                )
            }.collectLatest { state->
                _wallpaperState.update { state }
            }
        }
    }

    fun reloadWallpaper() {
        viewModelScope.launch {
            savedStateHandle.get<String>("wallpaperId")?.let { id ->
                getWallpaperById(id)
            }
        }
    }

    fun toggleFavourite(wallpaper: Wallpaper,isFavourite:Boolean){
        if(isFavourite){
            addWallpaperToFavourites(wallpaper)
        }else{
            removeWallpaperFromFavourites(wallpaper)
        }
    }

    init {
        viewModelScope.launch {
            savedStateHandle.getStateFlow("wallpaperId", "").collectLatest { id ->
                if (id.isNotEmpty()) {
                    combine(
                        wallpaperApiRepository.getWallpaperById(id),
                        wallpaperDatabaseRepository.getFavouriteWallpapers()
                    ) { wallpaperState, savedWallpapers ->

                        val wallpaper = wallpaperState.getSuccessDataOrNull()
                        val isFavourite = savedWallpapers.firstOrNull { it.id == id } != null

                        val savableWallpaper = SavableWallpaper(
                            wallpaper = wallpaper,
                            isFavourite = isFavourite
                        )

                        WallpaperDetailsState(
                            savableWallpaper = savableWallpaper,
                            isLoading = wallpaperState.isLoading(),
                            error = wallpaperState.getErrorMessageOrNull() ?: ""
                        )
                    }.collectLatest { state->
                        Log.e("WDVM",state.savableWallpaper.isFavourite.toString())
                        _wallpaperState.update { state }
                    }
                }
            }
        }
    }


    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    fun addWallpaperToFavourites(wallpaper: Wallpaper) {
        viewModelScope.launch(Dispatchers.IO) {
            wallpaperDatabaseRepository.insertWallpaper(wallpaper)
        }
    }

    private fun removeWallpaperFromFavourites(wallpaper: Wallpaper) {
        viewModelScope.launch(Dispatchers.IO) {
            wallpaperDatabaseRepository.deleteWallpaper(wallpaper)
        }
    }

}

