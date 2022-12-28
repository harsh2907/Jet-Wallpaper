package com.example.jetwallpaper.ui.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.domain.repository.WallpaperRepository
import com.example.jetwallpaper.domain.utils.Constants
import com.example.jetwallpaper.ui.presentation.utils.WallpapersScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WallpaperRepository
) : ViewModel() {

    val searchPager:Flow<PagingData<Wallpaper>>
    var accept:(UiAction)->Unit

    init {
        loadSavedWallpapers()

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

    var currentWallpaper: Wallpaper? = null

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val newPager = repository.getNewWallpapers().cachedIn(viewModelScope)

    private val _savedWallpapers = MutableStateFlow(WallpapersScreenState())
    val savedWallpapers = _savedWallpapers.asStateFlow()

    private fun searchWallpaper(query: String): Flow<PagingData<Wallpaper>> {
      return repository.getSearchedWallpapers(query)
    }

    private fun loadSavedWallpapers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavouriteWallpapers().collectLatest { wallpapers ->
                _savedWallpapers.value = savedWallpapers.value.copy(
                    wallpapers = wallpapers,
                    isLoading = false,
                    error = ""
                )
            }
        }
    }

    fun addWallpaper(wallpaper: Wallpaper){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertWallpaper(wallpaper)
        }
    }

    fun deleteWallpaper(wallpaper: Wallpaper){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteWallpaper(wallpaper)
        }
    }

    fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    fun updateSearchList(query: String,onQueryChanged:(UiAction)->Unit){
        onQueryChanged(UiAction.Search(query.trim()))
    }

    companion object {
        const val PAGE_SIZE = 24
    }

}

sealed class UiEvent{
    data class ShowSnackBar(
        val message:String,
        val action:String?= null
    ) :UiEvent()

    object Idle:UiEvent()

}

sealed class UiAction{
    data class Search(val query: String):UiAction()
}
