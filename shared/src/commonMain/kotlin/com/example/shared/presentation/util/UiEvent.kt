package com.example.shared.presentation.util

sealed class UiEvent{
    data class ShowSnackBar(
        val message:String,
        val action:String?= null
    ) :UiEvent()

    data object Idle:UiEvent()

}

sealed class UiAction{
    data class Search(val query: String):UiAction()
}
