package com.hackaprende.hackaweather.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaprende.hackaweather.R
import com.hackaprende.hackaweather.api.ApiResponseStatus
import com.hackaprende.hackaweather.common.DayForecast
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class MainViewModel: ViewModel() {
    private var _forecasts = MutableStateFlow<List<DayForecast>>(mutableListOf())
    val forecasts: StateFlow<List<DayForecast>> = _forecasts

    private var _responseStatus = MutableLiveData<ApiResponseStatus>()
    val responseStatus: LiveData<ApiResponseStatus>
        get() = _responseStatus

    private val repository = ForecastRepository()

    fun getForecasts(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _responseStatus.value = ApiResponseStatus.OnLoading
            clearList()
            repository.getDayForecasts(latitude, longitude)
                .catch { exception ->
                    if (exception is UnknownHostException) {
                        viewModelScope.cancel("No internet connection", exception)
                        _responseStatus.value = ApiResponseStatus.OnError(R.string.no_internet_connection)
                    }
                }
                .collect { dayForecast ->
                _forecasts.value = _forecasts.value.toMutableList().also {
                    _responseStatus.value = ApiResponseStatus.OnSuccess
                    it.add(dayForecast)
                }
            }
        }
    }

    private fun clearList() {
        if (_forecasts.value.isNotEmpty()) {
            _forecasts.value.toMutableList().also {
                it.clear()
            }
        }
    }
}