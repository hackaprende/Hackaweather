package com.hackaprende.hackaweather.api

sealed class ApiResponseStatus {
    object OnSuccess : ApiResponseStatus()
    object OnLoading : ApiResponseStatus()
    class OnError(val message: Int): ApiResponseStatus()
}
