package com.pwr.sailapp.data.network.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pwr.sailapp.data.weather.Currently
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherNetworkDataSourceImpl(
    private val darkSkyApiService: DarkSkyApiService
) : WeatherNetworkDataSource {

    private val _downloadedWeather = MutableLiveData<Currently>()

    override val downloadedWeather: LiveData<Currently>
        get() = _downloadedWeather


    override suspend fun fetchWeather(latitude: String, longitude: String, timestamp: String) {
        withContext(Dispatchers.IO){
            val response = darkSkyApiService.getForecastAsync(latitude, longitude, timestamp).await()
            _downloadedWeather.postValue(response.currently)
        }
    }
}