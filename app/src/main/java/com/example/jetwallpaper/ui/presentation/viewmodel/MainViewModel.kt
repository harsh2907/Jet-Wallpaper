package com.example.jetwallpaper.ui.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.jetwallpaper.domain.models.Wallpaper
import com.example.jetwallpaper.domain.repository.WallpaperRepository
import com.example.jetwallpaper.domain.utils.Response
import com.example.jetwallpaper.ui.pagination.NewWallpaperPagingSource
import com.example.jetwallpaper.ui.pagination.PopularWallpaperPagingSource
import com.example.jetwallpaper.ui.pagination.SearchPagingSource
import com.example.jetwallpaper.ui.presentation.utils.WallpapersScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.http.Query
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WallpaperRepository
) : ViewModel() {


    init {
        loadSavedWallpapers()
    }

    var currentWallpaper: Wallpaper? = null

    val popularPager = repository.getPopularWallpapers(PAGE_SIZE).cachedIn(viewModelScope)

    val newPager = repository.getNewWallpapers(PAGE_SIZE).cachedIn(viewModelScope)

    private val _savedWallpapers = MutableStateFlow(WallpapersScreenState())
    val savedWallpapers = _savedWallpapers.asStateFlow()


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

    companion object {
        const val PAGE_SIZE = 12
    }

}
