package com.hackaprende.hackaweather.common

import java.text.SimpleDateFormat
import java.util.*

private const val DAY_FORECAST_FORMAT = "yyyy/MMM/dd"

data class DayForecast(val hourly: List<HourlyForecast>) {
    var currentDay: String = ""

    fun setCurrentDayByTimestamp(timestamp: Long) {
        val simpleDateFormat = SimpleDateFormat(DAY_FORECAST_FORMAT, Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        currentDay = simpleDateFormat.format(timestamp * 1000)
    }
}