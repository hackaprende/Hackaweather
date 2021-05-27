package com.hackaprende.hackaweather.common

import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.*

private const val HOUR_FORMAT = "HH:mm"

data class HourlyForecast(val dt: Long, val temp: Double, @field:Json(name = "weather") val weatherList: List<ForecastWeather>) {
    val weather: ForecastWeather
        get() = weatherList[0]

    val formattedHour: String
        get() {
            val simpleDateFormat = SimpleDateFormat(HOUR_FORMAT, Locale.getDefault())
            simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            return simpleDateFormat.format(dt * 1000)
        }
}