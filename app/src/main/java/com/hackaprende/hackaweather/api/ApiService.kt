package com.hackaprende.hackaweather.api

import com.hackaprende.hackaweather.common.DayForecast
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit = Retrofit.Builder()
    .baseUrl("https://api.openweathermap.org/data/2.5/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

interface ApiService {
    @GET("onecall/timemachine")
    suspend fun getDayForecast(@Query("lat") latitude: Double, @Query("lon") longitude: Double,
                               @Query("dt") timestamp: Long, @Query("appid") appId: String,
                               @Query("units") units: String = "metric"): DayForecast
}

object ForecastApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}