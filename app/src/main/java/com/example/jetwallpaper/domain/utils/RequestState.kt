package com.example.jetwallpaper.domain.utils

sealed class RequestState<out T> {
    data object Loading : RequestState<Nothing>()
    data class Success<T>(val data: T) : RequestState<T>()
    data class Error(val message: String) : RequestState<Nothing>()

    fun isLoading() = this is Loading
    fun isSuccess() = this is Success
    fun isError() = this is Error

    /**
     * Returns data from a [Success].
     * @throws ClassCastException If the current state is not [Success]
     *  */
    fun getSuccessData() = (this as Success).data

    /**
     * Returns data from a [Error].
     * @throws ClassCastException If the current state is not [Error]
     *  */
    fun getErrorMessage() = (this as Error).message
    fun getSuccessDataOrNull(): T? = (this as? Success)?.data

    fun getErrorMessageOrNull(): String? = (this as? Error)?.message


}