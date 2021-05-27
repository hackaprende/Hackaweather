package com.hackaprende.hackaweather.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaprende.hackaweather.common.DayForecast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private var _forecasts = MutableStateFlow<List<DayForecast>>(mutableListOf())

    val forecasts: StateFlow<List<DayForecast>> = _forecasts

    private val repository = ForecastRepository()

    fun getForecasts(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            repository.getDayForecasts(latitude, longitude).collect {
                dayForecast ->
                _forecasts.value = _forecasts.value.toMutableList().also {
                    it.add(dayForecast)
                }
            }
        }
    }
}