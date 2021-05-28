package com.hackaprende.hackaweather.main

import com.hackaprende.hackaweather.CoroutinesTestRule
import com.hackaprende.hackaweather.FakeForecastRepository
import com.hackaprende.hackaweather.FakeLocationRepository
import com.hackaprende.hackaweather.api.FakeForecastRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `Downloading forecasts from web`() = runBlockingTest {
        val forecastRepository = ForecastRepository(FakeForecastRemoteDataSource())

        val dayForecast = forecastRepository.getDayForecasts(100.0, 12.12)
        dayForecast.collect {
            Assert.assertEquals(30.1, it.hourly.first().temp, 0.0)
        }
    }

    /**
     * This is not passing because of an issue reported here:
     * https://github.com/Kotlin/kotlinx.coroutines/issues/1204
     *
     * It gives this error:
     * "This job has not completed yet"
     *
     * Last time tried in this version of coroutines test dependency:
     * testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.3'
     */
    /*@Test
    fun downloadForecasts() = runBlockingTest {
        val forecastRepository = FakeForecastRepository()
        val locationRepository = FakeLocationRepository(null)

        val viewModel = MainViewModel(forecastRepository, locationRepository)
        viewModel.getForecasts(100.1, 12.12)
        viewModel.forecasts.collect {
            Assert.assertEquals(1, it.size)
        }
    }*/


}