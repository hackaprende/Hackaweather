package com.hackaprende.hackaweather.api

import com.hackaprende.hackaweather.common.DayForecast
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

private val retrofit = Retrofit.Builder()
    .baseUrl("https://api.openweathermap.org/data/2.5/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

private interface ApiService {
    @GET("onecall/timemachine")
    suspend fun getDayForecast(@Query("lat") latitude: Double, @Query("lon") longitude: Double,
                               @Query("dt") timestamp: Long, @Query("appid") appId: String,
                               @Query("units") units: String = "metric"): DayForecast
}

private object ForecastApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

class ForecastRemoteDataSource @Inject constructor() : RemoteDataSource {
    override suspend fun getDayForecast(
        latitude: Double,
        longitude: Double,
        timestampToDownload: Long,
        appId: String,
        units: String
    ) = ForecastApi.retrofitService.getDayForecast(latitude, longitude, timestampToDownload, appId)
}