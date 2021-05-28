package com.hackaprende.hackaweather.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hackaprende.hackaweather.R
import com.hackaprende.hackaweather.api.ApiResponseStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*


private const val LOCATION_PERMISSION_REQUEST_CODE = 1000

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location

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

        lifecycleScope.launchWhenStarted {
            mainViewModel.location.collect {
                if (it == null) {
                    // This is a temporal mock latitude due to problems with the GPS emulator
                    // it should not be in production
                    currentLocation = Location("")
                    currentLocation.latitude = 19.34
                    currentLocation.longitude = -99.15
                } else {
                    currentLocation = it
                }

                getForecasts(currentLocation.latitude, currentLocation.longitude)
                loadMap()
            }
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel.responseStatus.collect {
                when (it) {
                    is ApiResponseStatus.OnError -> {
                        binding.loadingWheel.visibility = View.GONE
                        binding.emptyView.text = getString(it.message)
                        binding.emptyView.visibility = View.VISIBLE
                    }
                    is ApiResponseStatus.OnLoading -> {
                        binding.loadingWheel.visibility = View.VISIBLE
                        binding.emptyView.text = getString(R.string.no_items_found)
                        binding.emptyView.visibility = View.GONE
                    }
                    is ApiResponseStatus.OnSuccess -> {
                        binding.loadingWheel.visibility = View.GONE
                        binding.emptyView.text = getString(R.string.no_items_found)
                        binding.emptyView.visibility = View.GONE
                    }
                    is ApiResponseStatus.None -> { /** Do nothing **/ }
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
    private fun requestUserLocation() = mainViewModel.getUserLocation(fusedLocationClient)

    private fun loadMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getForecasts(latitude: Double, longitude: Double) {
        binding.cityName.text = getCityNameByCoordinates(latitude, longitude)

        mainViewModel.getForecasts(latitude, longitude)
    }

    private fun getCityNameByCoordinates(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)

        return (if (addresses.isNullOrEmpty()) {
            getString(R.string.not_in_a_city)
        } else {
            addresses[0].locality
        }) ?: getString(R.string.not_in_a_city)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener {
            mMap.clear()
            addMarkerWithLatLng(it)
            getForecasts(it.latitude, it.longitude)
        }

        val currentLatLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        addMarkerWithLatLng(currentLatLng)
    }

    private fun addMarkerWithLatLng(latLng: LatLng) {
        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Marker in ${getCityNameByCoordinates(latLng.latitude, latLng.longitude)}"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5.0f))
    }
}