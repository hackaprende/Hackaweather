package com.hackaprende.hackaweather.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hackaprende.hackaweather.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import android.location.Geocoder
import com.hackaprende.hackaweather.R
import com.hackaprende.hackaweather.api.ApiResponseStatus
import java.util.*


private const val LOCATION_PERMISSION_REQUEST_CODE = 1000

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dayForecastAdapter = DayForecastAdapter()
        binding.forecastRecycler.adapter = dayForecastAdapter

        lifecycleScope.launchWhenStarted {
            mainViewModel.forecasts.collect {
                binding.emptyView.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                dayForecastAdapter.submitList(it)
            }
        }

        mainViewModel.responseStatus.observe(this) {
            when (it) {
                is ApiResponseStatus.OnError -> {
                    binding.loadingWheel.visibility = View.GONE
                    binding.emptyView.text = getString(it.message)
                    binding.emptyView.visibility = View.VISIBLE
                }
                ApiResponseStatus.OnLoading -> {
                    binding.loadingWheel.visibility = View.VISIBLE
                    binding.emptyView.text = getString(R.string.no_items_found)
                    binding.emptyView.visibility = View.GONE
                }
                ApiResponseStatus.OnSuccess -> {
                    binding.loadingWheel.visibility = View.GONE
                    binding.emptyView.text = getString(R.string.no_items_found)
                    binding.emptyView.visibility = View.GONE
                }
            }
        }

        handleLocationPermission()
    }

    private fun handleLocationPermission() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        requestUserLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults[0] == 1) {
            requestUserLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestUserLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location == null) {
                    // This is a temporal mock latitude due to problems with the GPS emulator
                        // it should not be in production
                    getForecasts(19.34,-99.15)
                } else {
                    getForecasts(location.latitude, location.longitude)
                }
            }
    }

    private fun getForecasts(latitude: Double, longitude: Double) {
        val gcd = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> = gcd.getFromLocation(latitude, longitude, 1)
        if (addresses.isNotEmpty()) {
            binding.cityName.text = addresses[0].locality
        }

        mainViewModel.getForecasts(latitude, longitude)
    }
}