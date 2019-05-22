package com.pwr.sailapp.data.network.weather

import androidx.lifecycle.LiveData
import com.pwr.sailapp.data.weather.Currently

interface WeatherNetworkDataSource {
    val downloadedWeather : LiveData<Currently> // Currently means
    fun fetchWeather(latitude: String, longitude: String, timestamp: String)
}