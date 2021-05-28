package com.hackaprende.hackaweather.api

import javax.inject.Inject

class ForecastRemoteDataSource @Inject constructor() : RemoteDataSource {
    override suspend fun getDayForecast(
        latitude: Double,
        longitude: Double,
        timestampToDownload: Long,
        appId: String,
        units: String
    ) = ForecastApi.retrofitService.getDayForecast(latitude, longitude, timestampToDownload, appId)
}