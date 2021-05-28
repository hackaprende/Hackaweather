package com.hackaprende.hackaweather.main

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class ForecastTasksModule {

    @Binds
    abstract fun getForecastTasks(forecastRepository: ForecastRepository): ForecastTasks
}