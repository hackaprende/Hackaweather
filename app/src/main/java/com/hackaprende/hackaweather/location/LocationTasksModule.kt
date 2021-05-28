package com.hackaprende.hackaweather.location

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class LocationTasksModule {

    @Binds
    abstract fun getLocationTasks(locationRepository: LocationRepository): LocationTasks
}