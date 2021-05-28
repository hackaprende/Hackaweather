package com.hackaprende.hackaweather.api

import com.hackaprende.hackaweather.common.DayForecast

interface RemoteDataSource {

    suspend fun getDayForecast(
        latitude: Double, longitude: Double, timestampToDownload: Long, appId: String,
        units: String = "metric"
    ): DayForecast
}