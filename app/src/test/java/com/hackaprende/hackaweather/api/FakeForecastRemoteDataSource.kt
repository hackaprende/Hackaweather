package com.hackaprende.hackaweather.api

import com.hackaprende.hackaweather.common.DayForecast
import com.hackaprende.hackaweather.common.ForecastWeather
import com.hackaprende.hackaweather.common.HourlyForecast
import java.util.*
import javax.inject.Inject

private const val ONE_HOUR_IN_SECONDS = 60 * 60

class FakeForecastRemoteDataSource @Inject constructor() : RemoteDataSource {
    override suspend fun getDayForecast(
        latitude: Double,
        longitude: Double,
        timestampToDownload: Long,
        appId: String,
        units: String
    ) : DayForecast {
        val mockWeather = ForecastWeather("Clear", "01n")
        val mockWeatherList = listOf(mockWeather)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val dt = calendar.time.time / 1000
        val hourForecast1 = HourlyForecast(dt, 30.1, mockWeatherList)
        val hourForecast2 = HourlyForecast(dt + ONE_HOUR_IN_SECONDS, 30.1, mockWeatherList)
        val mockHourlyForecastList = listOf(hourForecast1, hourForecast2)

        return DayForecast(mockHourlyForecastList)
    }
}