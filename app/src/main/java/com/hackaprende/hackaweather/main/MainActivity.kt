package com.hackaprende.hackaweather.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.hackaprende.hackaweather.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dayForecastAdapter = DayForecastAdapter()
        binding.forecastRecycler.adapter = dayForecastAdapter

        lifecycleScope.launchWhenStarted {
            mainViewModel.forecasts.collect {
                binding.emptyView.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                dayForecastAdapter.submitList(it)
            }
        }
    }
}