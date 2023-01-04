package com.example.weatherforecastapp.pojo

sealed class ViewState<out Any> {
    object Loading : ViewState<Nothing>()
    object NetworkError : ViewState<Nothing>()
    data class Success<Any>(val data: Any) : ViewState<Any>()
    data class Error(val data: String) : ViewState<Nothing>()
}
