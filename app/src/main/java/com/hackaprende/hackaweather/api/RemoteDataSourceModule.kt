package com.hackaprende.hackaweather.api

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RemoteDataSourceModule {

    @Binds
    @Singleton
    abstract fun getForecastDay(forecastRemoteDataSource: ForecastRemoteDataSource): RemoteDataSource
}