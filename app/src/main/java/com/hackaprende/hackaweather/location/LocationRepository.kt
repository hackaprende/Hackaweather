package com.hackaprende.hackaweather.location

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationRepository {

    @ExperimentalCoroutinesApi
    @SuppressLint("MissingPermission")
    fun getUserLocation(fusedLocationProviderClient: FusedLocationProviderClient): Flow<Location?> = callbackFlow {
        var onLocationSuccessListener: (OnSuccessListener<Location?>)? =
            OnSuccessListener<Location?> {
                offer(it)
            }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener(onLocationSuccessListener!!)

        awaitClose {
            onLocationSuccessListener = null
        }
    }
}