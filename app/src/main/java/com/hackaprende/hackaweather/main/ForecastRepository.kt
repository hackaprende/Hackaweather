package com.hackaprende.hackaweather.main

import com.hackaprende.hackaweather.api.ForecastApi
import com.hackaprende.hackaweather.common.DayForecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

private const val SECONDS_IN_ONE_DAY = 60 * 60 * 24

class ForecastRepository {

    val dayForecasts: Flow<DayForecast> = flow {
        var count = 1
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val currentTimestamp = calendar.time.time / 1000
        while(count < 6) {
            val timestampToDownload = currentTimestamp - (count * SECONDS_IN_ONE_DAY)
            val dayForecast = ForecastApi.retrofitService.getDayForecast(
                60.99, 30.9,
                timestampToDownload, "5dd487edb1886dda6965a9c828736c8b"
            )
            dayForecast.setCurrentDayByTimestamp(timestampToDownload)

            emit(dayForecast)
            count++
        }
    }
}