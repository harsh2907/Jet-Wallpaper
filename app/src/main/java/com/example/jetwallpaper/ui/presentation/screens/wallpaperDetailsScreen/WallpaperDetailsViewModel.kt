package com.example.jetwallpaper.ui.presentation.screens.wallpaperDetailsScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetwallpaper.domain.repository.WallpaperApiRepository
import com.example.jetwallpaper.domain.utils.RequestState
import com.example.jetwallpaper.ui.presentation.screens.main.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallpaperDetailsViewModel @Inject constructor(
    private val wallpaperApiRepository: WallpaperApiRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _wallpaperState = MutableStateFlow(WallpaperDetailsState())
    val wallpaperDetailsState = _wallpaperState.asStateFlow()

    fun getWallpaperById(id: String) {
        viewModelScope.launch {
            wallpaperApiRepository.getWallpaperById(id).collectLatest { res ->
                when (res) {
                    is RequestState.Success -> {
                        _wallpaperState.update {
                            it.copy(
                                isLoading = false,
                                wallpaper = res.getSuccessDataOrNull()
                            )
                        }
                    }

                    is RequestState.Error -> {
                        _wallpaperState.update {
                            it.copy(
                                isLoading = false,
                                error = res.getErrorMessage()
                            )
                        }
                    }

                    RequestState.Loading -> _wallpaperState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun reloadWallpaper(){
        savedStateHandle.get<String>("wallpaperId")?.let { id->
            getWallpaperById(id)
        }
    }

    init {
        viewModelScope.launch {
            savedStateHandle.getStateFlow("wallpaperId","").collectLatest { id->
                if(id.isNotEmpty()){
                    getWallpaperById(id)
                }
            }
        }
    }


    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

}