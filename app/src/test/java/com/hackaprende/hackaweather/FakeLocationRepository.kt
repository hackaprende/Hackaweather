package com.hackaprende.hackaweather

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.hackaprende.hackaweather.location.LocationTasks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class FakeLocationRepository(private val location: Location?): LocationTasks {
    @ExperimentalCoroutinesApi
    override fun getUserLocation(fusedLocationProviderClient: FusedLocationProviderClient): Flow<Location?> =
        flow {
            emit(location)
        }
}