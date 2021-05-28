package com.hackaprende.hackaweather.location

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.Flow

interface LocationTasks {

    fun getUserLocation(fusedLocationProviderClient: FusedLocationProviderClient): Flow<Location?>
}