package com.hackaprende.hackaweather.main

import com.hackaprende.hackaweather.common.DayForecast
import kotlinx.coroutines.flow.Flow

interface ForecastTasks {

    fun getDayForecasts(latitude: Double, longitude: Double): Flow<DayForecast>
}