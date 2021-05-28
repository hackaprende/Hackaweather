package com.hackaprende.hackaweather.main

import com.hackaprende.hackaweather.api.RemoteDataSource
import com.hackaprende.hackaweather.common.Constants.NUMBER_OF_DAYS_TO_DOWNLOAD
import com.hackaprende.hackaweather.common.DayForecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

private const val SECONDS_IN_ONE_DAY = 60 * 60 * 24

class ForecastRepository @Inject constructor(private val forecastRemoteDataSource: RemoteDataSource) :
    ForecastTasks {

    override fun getDayForecasts(latitude: Double, longitude: Double): Flow<DayForecast> = flow {
        var count = 0
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val currentTimestamp = calendar.time.time / 1000
        while(count < NUMBER_OF_DAYS_TO_DOWNLOAD) {
            val timestampToDownload = currentTimestamp - (count * SECONDS_IN_ONE_DAY)
            val dayForecast = forecastRemoteDataSource.getDayForecast(
                latitude, longitude,
                timestampToDownload, "5dd487edb1886dda6965a9c828736c8b"
            )
            dayForecast.setCurrentDayByTimestamp(timestampToDownload)

            emit(dayForecast)
            count++
        }
    }
}