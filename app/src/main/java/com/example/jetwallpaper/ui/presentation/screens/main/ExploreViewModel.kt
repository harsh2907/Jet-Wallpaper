package com.example.jetwallpaper.ui.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.jetwallpaper.data.utils.Constants
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.domain.repository.WallpaperApiRepository
import com.example.jetwallpaper.domain.repository.WallpaperDatabaseRepository
import com.example.jetwallpaper.ui.presentation.utils.WallpapersScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val wallpaperApiRepository: WallpaperApiRepository,
    private val wallpaperDatabaseRepository: WallpaperDatabaseRepository,
) : ViewModel() {

    val searchPager:Flow<PagingData<Wallpaper>>
    var accept:(UiAction)->Unit

    init {
        loadSavedWallpapers()

        //Initial Query
        val initialQuery = Constants.wallpaperThemes.random()
        val actionStateFlow = MutableSharedFlow<UiAction>()
        val searches = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.Search(query = initialQuery)) }

        searchPager = searches
            .flatMapLatest { searchWallpaper(query = it.query) }
            .cachedIn(viewModelScope)

        accept = {action->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }

    }


    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

  //  val newPager = repository.getNewWallpapers().cachedIn(viewModelScope)

    private val _savedWallpapers = MutableStateFlow(WallpapersScreenState())
    val savedWallpapers = _savedWallpapers.asStateFlow()

    private fun searchWallpaper(
        query: String,
        sortingParams:String = Constants.SortingParams.VIEWS.paramName
    ): Flow<PagingData<Wallpaper>> {
      return wallpaperApiRepository.getSearchedWallpapers(query,sortingParams)
    }

    private fun loadSavedWallpapers() {
        viewModelScope.launch(Dispatchers.IO) {
            wallpaperDatabaseRepository.getFavouriteWallpapers()
                .collectLatest { wallpapers ->
                _savedWallpapers.value = savedWallpapers.value.copy(
                    wallpapers = wallpapers,
                    isLoading = false,
                    error = ""
                )
            }
        }
    }


    fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    fun updateSearchList(query: String,onQueryChanged:(UiAction)->Unit){
        onQueryChanged(UiAction.Search(query.trim()))
    }

    fun deleteWallpaper(wallpaper: Wallpaper){
        viewModelScope.launch(Dispatchers.IO) {
            wallpaperDatabaseRepository.deleteWallpaper(wallpaper)
        }
    }

}

sealed class UiEvent{
    data class ShowSnackBar(
        val message:String,
        val action:String?= null
    ) : UiEvent()

    data object Idle: UiEvent()

}

sealed class UiAction{
    data class Search(val query: String): UiAction()
}
