package com.example.jetwallpaper.ui.presentation.screens.newWallpapersScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.jetwallpaper.data.utils.Constants
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.domain.repository.WallpaperApiRepository
import com.example.jetwallpaper.ui.presentation.screens.main.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewWallpapersViewModel @Inject constructor(
    private val wallpaperApiRepository: WallpaperApiRepository
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    lateinit var wallpaperPager: Flow<PagingData<Wallpaper>>


    init {
        getWallpapers()
    }


    fun getWallpapers(
        sortingParams: String = Constants.SortingParams.popular
    ) {
        wallpaperPager = wallpaperApiRepository
            .getNewWallpapers(sortingParams)
            .cachedIn(viewModelScope)
    }


    fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

}