package com.example.shared.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetwallpaper.domain.utils.Response
import com.example.shared.domain.models.WallpaperItem
import com.example.shared.domain.repository.OnlineWallpaperRepository
import com.example.shared.presentation.WallpaperState
import com.example.shared.presentation.util.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SharedViewModel(
    private val onlineWallpaperRepository: OnlineWallpaperRepository
) : ViewModel() {

    private val _popularWallpapers = MutableStateFlow(WallpaperState())
    val popularWallpapers = _popularWallpapers.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    var currentWallpaper: WallpaperItem? = null
        private set


    init {
        getPopularWallpapers()
    }

    fun updateCurrentWallpaper(wallpaperItem: WallpaperItem){
        currentWallpaper = wallpaperItem
    }

    fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    fun getPopularWallpapers() {
        viewModelScope.launch {
            onlineWallpaperRepository.getPopularWallpapers().collectLatest { res ->
                when (res) {
                    is Response.Loading -> {
                        _popularWallpapers.update { it.copy(isLoading = true) }
                        println("Loadingg....")
                    }

                    is Response.Error -> {
                        _popularWallpapers.update { it.copy(isLoading = false, error = res.error) }
                        println("Error: ${res.error}")
                    }

                    is Response.Success -> {
                        println("Success: ${res.data.size}")

                        _popularWallpapers.update {
                            it.copy(
                                isLoading = false,
                                wallpapers = res.data
                            )
                        }
                    }
                }
            }
        }
    }
}