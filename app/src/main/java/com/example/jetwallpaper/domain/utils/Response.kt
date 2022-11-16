package com.example.jetwallpaper.domain.utils



sealed class Response<T>(
    private val data:T? = null,
    private val error:String? = null
){
    data class Success<T>(val data:T):Response<T>(data = data)
    data class Error<T>(val error: String):Response<T>(error = error)
    class Loading<T>() : Response<T>()
}