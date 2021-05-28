package com.hackaprende.hackaweather.main

import com.hackaprende.hackaweather.CoroutinesTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun myTest() {
        Assert.assertTrue(true)
    }
    /*@Test
    fun `Downloading forecasts from web`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        Assert.assertTrue(true)
    }*/
}